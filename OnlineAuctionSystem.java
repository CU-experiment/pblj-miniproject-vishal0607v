// Java code to demonstrate an Online Auction System

// Import necessary packages
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// User class to store user information
class User {
    private String userId;
    private String username;
    private String email;
    private String password;
    private double balance;

    public User(String username, String email, String password) {
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = 1000.0; // Default balance for demonstration
    }

    // Getters and setters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    
    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", balance=" + balance +
                '}';
    }
}

// Item class for auction items
class Item {
    private String itemId;
    private String name;
    private String description;
    private double startingPrice;
    private String sellerId;
    private String category;
    
    public Item(String name, String description, double startingPrice, String sellerId, String category) {
        this.itemId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.startingPrice = startingPrice;
        this.sellerId = sellerId;
        this.category = category;
    }
    
    // Getters
    public String getItemId() { return itemId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getStartingPrice() { return startingPrice; }
    public String getSellerId() { return sellerId; }
    public String getCategory() { return category; }
    
    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startingPrice=" + startingPrice +
                ", sellerId='" + sellerId + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}

// Bid class to track bids
class Bid {
    private String bidId;
    private String auctionId;
    private String bidderId;
    private double amount;
    private LocalDateTime timestamp;
    
    public Bid(String auctionId, String bidderId, double amount) {
        this.bidId = UUID.randomUUID().toString();
        this.auctionId = auctionId;
        this.bidderId = bidderId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getBidId() { return bidId; }
    public String getAuctionId() { return auctionId; }
    public String getBidderId() { return bidderId; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Bid{" +
                "bidId='" + bidId + '\'' +
                ", auctionId='" + auctionId + '\'' +
                ", bidderId='" + bidderId + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp.format(formatter) +
                '}';
    }
}

// Auction class to manage auctions
class Auction {
    private String auctionId;
    private String itemId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double currentHighestBid;
    private String currentHighestBidder;
    private boolean isActive;
    private List<Bid> bids;
    
    public Auction(String itemId, LocalDateTime startTime, LocalDateTime endTime) {
        this.auctionId = UUID.randomUUID().toString();
        this.itemId = itemId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.currentHighestBid = 0.0;
        this.currentHighestBidder = null;
        this.isActive = true;
        this.bids = new ArrayList<>();
    }
    
    // Getters and setters
    public String getAuctionId() { return auctionId; }
    public String getItemId() { return itemId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public double getCurrentHighestBid() { return currentHighestBid; }
    public String getCurrentHighestBidder() { return currentHighestBidder; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public List<Bid> getBids() { return bids; }
    
    public void addBid(Bid bid) {
        bids.add(bid);
        if (bid.getAmount() > currentHighestBid) {
            currentHighestBid = bid.getAmount();
            currentHighestBidder = bid.getBidderId();
        }
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Auction{" +
                "auctionId='" + auctionId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", startTime=" + startTime.format(formatter) +
                ", endTime=" + endTime.format(formatter) +
                ", currentHighestBid=" + currentHighestBid +
                ", currentHighestBidder='" + currentHighestBidder + '\'' +
                ", isActive=" + isActive +
                ", bids=" + bids.size() +
                '}';
    }
}

// AuctionSystem class to manage the entire system
class AuctionSystem {
    private Map<String, User> users;
    private Map<String, Item> items;
    private Map<String, Auction> auctions;
    private Map<String, List<String>> userAuctions; // user ID -> list of auction IDs
    private Map<String, List<String>> userBids; // user ID -> list of bid IDs
    private User currentUser;
    
    public AuctionSystem() {
        this.users = new HashMap<>();
        this.items = new HashMap<>();
        this.auctions = new HashMap<>();
        this.userAuctions = new HashMap<>();
        this.userBids = new HashMap<>();
        this.currentUser = null;
    }
    
    // User Management
    public User registerUser(String username, String email, String password) {
        // Check if username or email already exists
        for (User user : users.values()) {
            if (user.getUsername().equals(username) || user.getEmail().equals(email)) {
                System.out.println("Username or email already exists!");
                return null;
            }
        }
        
        User newUser = new User(username, email, password);
        users.put(newUser.getUserId(), newUser);
        userAuctions.put(newUser.getUserId(), new ArrayList<>());
        userBids.put(newUser.getUserId(), new ArrayList<>());
        return newUser;
    }
    
    public User login(String email, String password) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email) && user.verifyPassword(password)) {
                currentUser = user;
                return user;
            }
        }
        System.out.println("Invalid credentials!");
        return null;
    }
    
    public void logout() {
        currentUser = null;
        System.out.println("Logged out successfully!");
    }
    
    // Item Management
    public Item createItem(String name, String description, double startingPrice, String category) {
        if (currentUser == null) {
            System.out.println("You must be logged in to create an item!");
            return null;
        }
        
        Item newItem = new Item(name, description, startingPrice, currentUser.getUserId(), category);
        items.put(newItem.getItemId(), newItem);
        return newItem;
    }
    
    // Auction Management
    public Auction createAuction(String itemId, int durationInHours) {
        if (currentUser == null) {
            System.out.println("You must be logged in to create an auction!");
            return null;
        }
        
        Item item = items.get(itemId);
        if (item == null) {
            System.out.println("Item not found!");
            return null;
        }
        
        if (!item.getSellerId().equals(currentUser.getUserId())) {
            System.out.println("You can only auction your own items!");
            return null;
        }
        
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(durationInHours);
        
        Auction newAuction = new Auction(itemId, startTime, endTime);
        auctions.put(newAuction.getAuctionId(), newAuction);
        
        // Add to user's auctions
        userAuctions.get(currentUser.getUserId()).add(newAuction.getAuctionId());
        
        return newAuction;
    }
    
    // Bidding
    public Bid placeBid(String auctionId, double amount) {
        if (currentUser == null) {
            System.out.println("You must be logged in to place a bid!");
            return null;
        }
        
        Auction auction = auctions.get(auctionId);
        if (auction == null) {
            System.out.println("Auction not found!");
            return null;
        }
        
        Item item = items.get(auction.getItemId());
        if (item.getSellerId().equals(currentUser.getUserId())) {
            System.out.println("You cannot bid on your own auction!");
            return null;
        }
        
        if (!auction.isActive()) {
            System.out.println("This auction is no longer active!");
            return null;
        }
        
        if (LocalDateTime.now().isAfter(auction.getEndTime())) {
            auction.setActive(false);
            System.out.println("This auction has ended!");
            return null;
        }
        
        if (amount <= auction.getCurrentHighestBid()) {
            System.out.println("Your bid must be higher than the current highest bid!");
            return null;
        }
        
        if (amount > currentUser.getBalance()) {
            System.out.println("You don't have enough balance for this bid!");
            return null;
        }
        
        Bid newBid = new Bid(auctionId, currentUser.getUserId(), amount);
        auction.addBid(newBid);
        
        // Add to user's bids
        userBids.get(currentUser.getUserId()).add(newBid.getBidId());
        
        return newBid;
    }
    
    // Listing Auctions
    public List<Auction> listActiveAuctions() {
        List<Auction> activeAuctions = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (Auction auction : auctions.values()) {
            if (auction.isActive() && now.isBefore(auction.getEndTime())) {
                activeAuctions.add(auction);
            } else if (auction.isActive() && now.isAfter(auction.getEndTime())) {
                auction.setActive(false);
            }
        }
        
        return activeAuctions;
    }
    
    // View auction details with item information
    public void viewAuctionDetails(String auctionId) {
        Auction auction = auctions.get(auctionId);
        if (auction == null) {
            System.out.println("Auction not found!");
            return;
        }
        
        Item item = items.get(auction.getItemId());
        User seller = users.get(item.getSellerId());
        
        System.out.println("=== Auction Details ===");
        System.out.println("Item: " + item.getName());
        System.out.println("Description: " + item.getDescription());
        System.out.println("Category: " + item.getCategory());
        System.out.println("Seller: " + seller.getUsername());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Start Time: " + auction.getStartTime().format(formatter));
        System.out.println("End Time: " + auction.getEndTime().format(formatter));
        
        System.out.println("Current Highest Bid: $" + auction.getCurrentHighestBid());
        
        if (auction.getCurrentHighestBidder() != null) {
            User highestBidder = users.get(auction.getCurrentHighestBidder());
            System.out.println("Highest Bidder: " + highestBidder.getUsername());
        } else {
            System.out.println("No bids yet.");
        }
        
        System.out.println("Status: " + (auction.isActive() ? "Active" : "Closed"));
        
        System.out.println("\n--- Bid History ---");
        List<Bid> bids = auction.getBids();
        if (bids.isEmpty()) {
            System.out.println("No bids yet.");
        } else {
            for (int i = bids.size() - 1; i >= 0; i--) {
                Bid bid = bids.get(i);
                User bidder = users.get(bid.getBidderId());
                System.out.println(bid.getTimestamp().format(formatter) + 
                                   " - " + bidder.getUsername() + 
                                   ": $" + bid.getAmount());
            }
        }
    }
    
    // Close Auction
    public void closeAuction(String auctionId) {
        Auction auction = auctions.get(auctionId);
        if (auction == null) {
            System.out.println("Auction not found!");
            return;
        }
        
        Item item = items.get(auction.getItemId());
        if (!item.getSellerId().equals(currentUser.getUserId())) {
            System.out.println("You can only close your own auctions!");
            return;
        }
        
        auction.setActive(false);
        System.out.println("Auction closed successfully!");
        
        if (auction.getCurrentHighestBidder() != null) {
            User seller = users.get(item.getSellerId());
            User buyer = users.get(auction.getCurrentHighestBidder());
            
            // Transfer funds
            if (buyer.getBalance() >= auction.getCurrentHighestBid()) {
                buyer.setBalance(buyer.getBalance() - auction.getCurrentHighestBid());
                seller.setBalance(seller.getBalance() + auction.getCurrentHighestBid());
                
                System.out.println("Funds transferred from " + buyer.getUsername() + 
                                   " to " + seller.getUsername() + 
                                   " for $" + auction.getCurrentHighestBid());
            } else {
                System.out.println("Transaction failed! Buyer doesn't have enough funds.");
            }
        }
    }
    
    // Get current user
    public User getCurrentUser() {
        return currentUser;
    }
    
    // Get user by ID
    public User getUserById(String userId) {
        return users.get(userId);
    }
    
    // Get item by ID
    public Item getItemById(String itemId) {
        return items.get(itemId);
    }
}

// Main class to demonstrate the system
public class OnlineAuctionSystem {
    public static void main(String[] args) {
        // Create auction system
        AuctionSystem system = new AuctionSystem();
        
        // Register users
        User john = system.registerUser("john_doe", "john@example.com", "password123");
        User alice = system.registerUser("alice_smith", "alice@example.com", "securepass");
        User bob = system.registerUser("bob_johnson", "bob@example.com", "bobpass123");
        
        System.out.println("=== Users Created ===");
        System.out.println(john);
        System.out.println(alice);
        System.out.println(bob);
        System.out.println();
        
        // Login as John
        system.login("john@example.com", "password123");
        System.out.println("Logged in as: " + system.getCurrentUser().getUsername());
        
        // John creates items
        Item laptop = system.createItem("MacBook Pro", "Latest model with M2 chip", 1200.0, "Electronics");
        Item watch = system.createItem("Rolex Submariner", "Luxury watch in excellent condition", 8000.0, "Accessories");
        
        System.out.println("\n=== Items Created by John ===");
        System.out.println(laptop);
        System.out.println(watch);
        System.out.println();
        
        // John creates auctions
        Auction laptopAuction = system.createAuction(laptop.getItemId(), 24); // 24 hours
        Auction watchAuction = system.createAuction(watch.getItemId(), 48); // 48 hours
        
        System.out.println("=== Auctions Created by John ===");
        System.out.println(laptopAuction);
        System.out.println(watchAuction);
        System.out.println();
        
        // Logout John
        system.logout();
        
        // Login as Alice
        system.login("alice@example.com", "securepass");
        System.out.println("Logged in as: " + system.getCurrentUser().getUsername());
        
        // Alice places bid on laptop
        Bid aliceBid = system.placeBid(laptopAuction.getAuctionId(), 1300.0);
        
        System.out.println("\n=== Alice's Bid ===");
        System.out.println(aliceBid);
        System.out.println();
        
        // Logout Alice
        system.logout();
        
        // Login as Bob
        system.login("bob@example.com", "bobpass123");
        System.out.println("Logged in as: " + system.getCurrentUser().getUsername());
        
        // Bob places higher bid on laptop
        Bid bobBid = system.placeBid(laptopAuction.getAuctionId(), 1400.0);
        
        System.out.println("\n=== Bob's Bid ===");
        System.out.println(bobBid);
        System.out.println();
        
        // Logout Bob
        system.logout();
        
        // Login as Alice again
        system.login("alice@example.com", "securepass");
        System.out.println("Logged in as: " + system.getCurrentUser().getUsername());
        
        // Alice places even higher bid
        Bid aliceBid2 = system.placeBid(laptopAuction.getAuctionId(), 1500.0);
        
        System.out.println("\n=== Alice's New Bid ===");
        System.out.println(aliceBid2);
        System.out.println();
        
        // View auction details
        System.out.println("\n--- Laptop Auction Details ---");
        system.viewAuctionDetails(laptopAuction.getAuctionId());
        
        // Logout Alice
        system.logout();
        
        // Login as John
        system.login("john@example.com", "password123");
        System.out.println("\nLogged in as: " + system.getCurrentUser().getUsername());
        
        // John closes the laptop auction
        system.closeAuction(laptopAuction.getAuctionId());
        
        // Check balances
        System.out.println("\n=== Updated Balances ===");
        System.out.println("John's balance: $" + system.getUserById(john.getUserId()).getBalance());
        System.out.println("Alice's balance: $" + system.getUserById(alice.getUserId()).getBalance());
        System.out.println("Bob's balance: $" + system.getUserById(bob.getUserId()).getBalance());
        
        // View active auctions
        System.out.println("\n=== Active Auctions ===");
        List<Auction> activeAuctions = system.listActiveAuctions();
        for (Auction auction : activeAuctions) {
            Item item = system.getItemById(auction.getItemId());
            System.out.println(item.getName() + " - Current bid: $" + auction.getCurrentHighestBid());
        }
    }
}

// Execute the program
OnlineAuctionSystem.main(new String[]{});