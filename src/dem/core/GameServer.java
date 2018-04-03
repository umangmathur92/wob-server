package dem.core;

// Java Imports
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// Other Imports
import conf.Configuration;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import lby.core.badge.BadgeController;
import lby.core.world.WorldController;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.TimerTask;
import lby.MiniGameServers;
import dem.db.PlayerDAO;
import dem.db.EcosystemDAO;
import dem.db.SpeciesChangeListDAO;
import dem.metadata.Constants;
import dem.metadata.GameRequestTable;
import dem.model.Account;
import dem.model.Ecosystem;
import dem.model.Player;
import dem.util.ConfFileParser;
import dem.util.ConfigureException;
import dem.util.ExpTable;
import dem.util.GameTimer;
import dem.util.Log;

/**
 * The GameServer class serves as the main module that runs the server. Incoming
 * connection requests are established and redirected to be managed by another
 * class called the GameClient. Several specialized methods are also stored here
 * to perform other specific needs.
 */
public class GameServer {

    // Singleton Instance
    private static GameServer server;
    // Configuration Variables
    private final int port;
    private final int num_threads;
    // Objects
    private final ServerSocket serverSocket;
    private final List<ClientHandler> clientHandlerThreads = Collections.synchronizedList(new ArrayList<ClientHandler>());
    // Lookup Tables
    private final Map<String, GameClient> activeClients = new HashMap<String, GameClient>(); // Session ID -> Client
    private final Map<Integer, Account> activeAccounts = new HashMap<Integer, Account>(); // Account ID -> Account
    private final Map<Integer, Player> activePlayers = new HashMap<Integer, Player>(); // Player ID -> Player
    // Other
    private boolean isActive = true; // Server Loop Flag    
    private int mCount;
    private final GameTimer ecoUpdateTimer = new GameTimer();
    private static int world_id;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    /* This is the path name in the server. Must be updated if a new server path is used  */
    public final static String SERVER_PATH = "/project/wob_server";
    /* This is food web image block size to send to client. 
     * Must match value in client, WorldController class
     * Must be under 32K limit
     */
    public final static int FOOD_WEB_BLOCK_SIZE = 32000;
    
    

    /**
     * Create the GameServer by setting up the request types and creating a
     * connection with the database.
     *
     * @param port
     * @param num_threads
     * @throws IOException
     */
    public GameServer(int port, int num_threads) throws IOException {
        this.port = port;
        this.num_threads = num_threads;

        serverSocket = new ServerSocket(port);
    }

    public static GameServer getInstance() {
        return server;
    }

    /**
     * Configure tables.
     * @throws ConfigureException
     */
    public void configure() throws ConfigureException {
        // Initialize tables for global use
        ServerResources.init();
        GameRequestTable.init(); // Contains request codes and classes
        ExpTable.init(); // Contains experience required per level
        // Update Badge Thresholds
        WorldController.getInstance().init();
        BadgeController.setBadgeScores();
    }

    /**
     * Run the game server by waiting for incoming connection requests.
     * Establishes each connection and stores it into a GameClient to manage
     * incoming and outgoing activity.
     */
    private void run() {
        Log.consoleln("Now accepting connections...");
        
        
        // Loop indefinitely to establish multiple connections
        while (isActive) {
            try {
                // Accept the incoming connection from client
                Socket clientSocket = serverSocket.accept();
                Log.printf("%s is connecting...", clientSocket.getInetAddress().getHostAddress());
                // "Random" ID
                String session_id = UUID.randomUUID().toString();
                // Create a runnable instance to represent a client that holds the client socket
                GameClient client = new GameClient(session_id, clientSocket);
                activeClients.put(client.getID(), client);
                // Keep track of the new client thread
                if (clientHandlerThreads.size() > num_threads) {
                    Collections.sort(clientHandlerThreads, ClientHandler.SizeComparator);
                    clientHandlerThreads.get(0).add(client);
                } else {
                    ClientHandler handler = new ClientHandler(client);
                    handler.start();

                    clientHandlerThreads.add(handler);
                }
            } catch (IOException ex) {
                Log.println_e(ex.getMessage());
            }
        }
    }

    public void shutdown() {
        synchronized (this) {
            isActive = false;

            for (GameClient client : activeClients.values()) {
                client.end();
            }
        }
    }

    public int getPort() {
        return port;
    }

    public int getNumThreads() {
        return num_threads;
    }

    public void removeClientHandler(ClientHandler handler) {
        synchronized (clientHandlerThreads) {
            clientHandlerThreads.remove(handler);
        }
    }

    public GameClient getActiveClient(String session_id) {
        return activeClients.get(session_id);
    }

    public void setActiveClient(GameClient client) {
        activeClients.put(client.getID(), client);
    }

    public List<GameClient> getActiveClients() {
        return new ArrayList<GameClient>(activeClients.values());
    }

    public void removeActiveClient(String session_id) {
        activeClients.remove(session_id);
    }

    public boolean hasClient(String session_id) {
        return activeClients.containsKey(session_id);
    }

    public Account getActiveAccount(int account_id) {
        return activeAccounts.get(account_id);
    }

    public void setActiveAccount(Account account) {
        activeAccounts.put(account.getID(), account);
    }

    public List<Account> getActiveAccounts() {
        return new ArrayList<Account>(activeAccounts.values());
    }

    public void removeActiveAccount(int account_id) {
        activeAccounts.remove(account_id);
    }

    public boolean hasAccount(int account_id) {
        return activeAccounts.containsKey(account_id);
    }

    public Player getActivePlayer(int player_id) {
        return activePlayers.get(player_id);
    }

    public void setActivePlayer(Player player) {
        activePlayers.put(player.getID(), player);
    }

    public List<Player> getActivePlayers() {
        return new ArrayList<Player>(activePlayers.values());
    }

    public void removeActivePlayer(int player_id) {
        activePlayers.remove(player_id);
    }

    public boolean hasPlayer(int player_id) {
        return activePlayers.containsKey(player_id);
    }
    

    /**
     * Initiates the Game Server by configuring and running it. Restarts
     * whenever it crashes.
     *
     * @param args contains additional launching parameters
     */
    public static void main(String[] args) {
        Log.printf("World of Balance Lobby Server is starting on port: %d", Configuration.lobbyPortNumber);
        try {
            server = new GameServer(Configuration.lobbyPortNumber, Constants.MAX_CLIENT_THREADS);
            server.configure();            
            Log.println("WoB current day is " + SpeciesChangeListDAO.fetchDay());
            MiniGameServers.getInstance().runServers();
            world_id = WorldController.getInstance().first().getID();
            server.startEcosystemUpdate();
            Log.println("Start Ecosystem periodic update");            
            server.run();
        } catch (IOException ex) {
            Log.printf_e("Failed to start server. Port %d is already in use", Configuration.lobbyPortNumber);
        } catch (ConfigureException ex) {
            Log.printf_e(ex.getMessage());
        } catch (Exception ex) {
            Log.println_e("Server Crashed!");
            Log.println_e(ex.getMessage());
        }

        System.exit(0);
    }    
}
