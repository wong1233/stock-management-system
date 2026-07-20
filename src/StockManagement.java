package src;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StockManagement {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		LocalDateTime currentDate = LocalDateTime.now(); //display current Date

		
		// Formating the current date form.
		DateTimeFormatter Form = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss ");
		String Date = currentDate.format(Form);
		
		
		
		System.out.println("Welcome to SMS!");
		System.out.println(Date);
		System.out.println("------------------------------------------------------");
		System.out.println("Group member:");
		System.out.println("1. Hong Chee Ren");
		System.out.println("2. Lee Jing Wei");
		System.out.println("3. Lee Sun Sheng");
		System.out.println("4. Wong Ting Kai");
		System.out.println("------------------------------------------------------");

		// Allow user input full name
		UserInfo user = new UserInfo();
		user.readInput(input);
		
		// Ask user to add product or not
		int start = 0;
		do {
			try {
				System.out.print("Do you want to add products? (1 = Yes / 0 = No): ");
			    start = input.nextInt();
			    
			    if (start < 0 || start > 1)
			    	System.out.println("Invalid input! Number must be either 0 or 1 only.");
			    
			    System.out.println("------------------------------------------------------");
			    
			} catch(InputMismatchException e) {
				System.out.println("Invalid input! Please enter a number.");
				input.nextLine();
				start = -1;
			}
		} while(start < 0 || start > 1);
		


	    if (start == 0) {
	        System.out.println(user);
	        return;
	    }
	    
		int maxProductNum = getMaxProducts(input);
		Product[] products = new Product[maxProductNum];
		
		for(int i = 0; i < maxProductNum; i++) {
			addProduct(products, input, i);
		}
		
		int option;
		do {
			option = displayMenu(input);
			methodMenu(input, option, products);
			
		}while(option != 0);


		
		
		
		System.out.println(user);
		input.close();
		
	}
	
	public static void clearConsole() {
	    for (int i = 0; i < 50; i++) {
	        System.out.println();
	    }
	}
	
	
	// 1. get maximum number of products
	public static int getMaxProducts(Scanner input){ // Get Maximum number of products
		int max = 0;
		
		
		do{
			
			try {
				System.out.print("Please enter maximum number of products you want to store: ");
				max = input.nextInt();
				
				if(max < 1) {
					System.out.println("Please enter correct maximum number of products");
				}
			}catch(InputMismatchException e) {
				System.out.println("Invalid input! Please enter a number.");
				input.nextLine();
				max = -1;
			}
			
		}while(max < 1);
		
		
		
		return max;
	}
	
	
	
	// 3. displayMenu
	public static int displayMenu(Scanner input) { //Display the menu
		int option = -1;
		
		do {
			try {

				System.out.println("------------------------------------------------------");
				System.out.println("1. View products");
				System.out.println("2. Add stock");
				System.out.println("3. Deduct Stock");
				System.out.println("4. Discontinue product");
				System.out.println("0. Exit");
				System.out.print("Please enter a menu option: ");
				option = input.nextInt();
				if(option < 0 || option > 4) {
					System.out.println("Enter error! Please enter option 0 - 4.");
				}
				}catch (InputMismatchException e) {
				
					System.out.println("Invalid input! Please enter a number.");
					input.nextLine();
					option = -1;
				}
		}while(option < 0 || option > 4);
			

		
		return option;
	}
	
	
	public static void methodMenu(Scanner input, int option, Product[] products) {
		switch(option) {
		case 1:
			viewProduct(products);
			break;
		case 2:
			boolean valid2 = false;
			 do {
				 try {
					 addStock(products, input);
					 valid2 = true;
				 }catch(InputMismatchException e) {
					 System.out.println("Invalid input! Please enter a number.");
					 input.nextLine();
					 clearConsole();
				 }
			 }while(!valid2);
			break;
		case 3:
			boolean valid3 = false;
			 do {
				 try {
					 deductStock(products, input);
					 valid3 = true;
				 }catch(InputMismatchException e) {
					 System.out.println("Invalid input! Please enter a number.");
					 input.nextLine();
					 input.nextLine();
					 clearConsole();
				 }
			 }while(!valid3);
			break;
		case 4:
			boolean valid4 = false;
			 do {
				 try {
					 setStatus(products, input);
					 valid4 = true;
				 }catch(InputMismatchException e) {
					 System.out.println("Invalid input! Please enter a number.");
					 input.nextLine();
					 input.nextLine();
					 clearConsole();
				 }
			 }while(!valid4);
			break;
		default :
		}

		
	}
	
	
	public static void addProduct(Product[] products, Scanner input, int index) {
		int type = 0;
		do {
			
			System.out.println("------------------------------------------------------");
			System.out.println("1. Refrigerator");
			System.out.println("2. TV");
			System.out.println("3. Laptop");
			System.out.println("4. Washing Machine");
			System.out.print("Please enter your choice: ");

			try {
				type = input.nextInt();
				
				if (type < 1 || type > 4) {
					 System.out.println("Only number 1, 2, 3 or 4 allowed!");
				}	
			}catch(InputMismatchException e) {
				System.out.println("Invalid input! Please enter a number.");
				 input.nextLine();
				 
				 type = 0;
			}
			
			System.out.println("------------------------------------------------------");
				
		}while(type < 1 || type > 4);
		
		// 1. addRefrigerator
		 if (type == 1) {
			 boolean valid = false;
			 do {
				 try {
					 addRefrigerator(products, input, index);
					 valid = true;
				 }catch(InputMismatchException e) {
					 System.out.println("Invalid input! Please enter correctly");
					 input.nextLine();
					 input.nextLine();
					 clearConsole();
				 }
			 }while(!valid);
		 }
		 
		 //2. addTV
		 else if (type == 2) {
			 boolean valid = false;
			 do {
				 try {
					 addTV(products, input, index);
					 valid = true;
				 } catch(InputMismatchException e) {
					 System.out.println("Invalid input! Please enter correctly");
					 input.nextLine();
					 input.nextLine();
					 clearConsole();
				 }
			 } while(!valid);
		 }
		 
		 //3. addLaptop
		 else if(type == 3) {
			 boolean valid = false;
			 do {
				 try {
					 addLaptop(products, input, index);
					 valid = true;
				 } catch(InputMismatchException e) {
					 System.out.println("Invalid input! Please enter correctly");
					 input.nextLine();
					 input.nextLine();
					 clearConsole();
				 }
			 } while(!valid);
		 }
		 else {
			 boolean valid4 = false;
			 do {
				 try {
					 addWashingMachine(products, input, index);
					 valid4 = true;
				 } catch(InputMismatchException e) {
					 System.out.println("Invalid input! Please enter correctly");
					 input.nextLine();
					 input.nextLine();
					 clearConsole();
				 }
			 } while(!valid4);
		 }

		 

	}
	
	public static void addRefrigerator(Product[] products, Scanner input, int index) {
	    
	    input.nextLine(); // for clear buffer
	    
		System.out.print("Enter product name: ");
		String name = input.nextLine();
		while (name.trim().isEmpty()) {
		    System.out.println("Error: Product name cannot be empty!");
		    System.out.print("Enter product name: ");
		    name = input.nextLine();
		}
		
		String doorDesign;
		boolean validDoor;
		do {
		    System.out.print("Enter door design (Single, Double, Side, French, Top, Bottom): ");
		    doorDesign = input.nextLine().toUpperCase();

		    switch (doorDesign) {
		        case "SINGLE":
		        case "DOUBLE":
		        case "SIDE":
		        case "FRENCH":
		        case "TOP":
		        case "BOTTOM":
		            validDoor = true;
		            break;
		        default:
		            validDoor = false;
		            System.out.println("Invalid door design! Please try again.");
		    }

		} while (!validDoor);
		
		
		
		System.out.print("Enter color: ");
	    String color = input.nextLine();
		while (color.trim().isEmpty()) {
		    System.out.println("Error: Color cannot be empty!");
		    System.out.print("Enter color: ");
		    color = input.nextLine();
		}
	    

	    System.out.print("Enter capacity: ");
	    double capacity = input.nextDouble();

	    System.out.print("Enter quantity: ");
	    int qty = input.nextInt();

	    System.out.print("Enter price: ");
	    double price = input.nextDouble();
	    
		System.out.print("Enter item number: ");
	    int itemNum = input.nextInt();
	    
	    input.nextLine();
	    
	    products[index] = new Refrigerator(name, itemNum, price, qty, doorDesign, color, capacity);
		
	    System.out.println("Refrigerator added successfully!");
	    
	}
	
	public static void addTV(Product[] products, Scanner input, int index) {
		input.nextLine(); // for clear buffer
	    
	    System.out.print("Enter product name: ");
	    String name = input.nextLine();
		while (name.trim().isEmpty()) {
		    System.out.println("Error: Product name cannot be empty!");
		    System.out.print("Enter product name: ");
		    name = input.nextLine();
		}

	    String[] validTypes = {"LED", "LCD", "OLED", "QLED"};
	    String screenType;
	    boolean valid = false;

	    do {
	        System.out.print("Enter screen type (LED, LCD, OLED, QLED): ");
	        screenType = input.nextLine().toUpperCase();

	        for (String type : validTypes) {
	            if (screenType.equals(type)) {
	                valid = true;
	                break;
	            }
	        }

	        if (!valid) {
	            System.out.println("Invalid screen type!");
	        }

	    } while (!valid);
	    
	    String resolution;
	    boolean validRes = false;

	    do {
	        System.out.print("Enter resolution (HD, FHD, 2K, 4K, 8K): ");
	        resolution = input.nextLine().toUpperCase();

	        switch (resolution) {
	            case "HD":
	            case "FHD":
	            case "2K":
	            case "4K":
	            case "8K":
	                validRes = true;
	                break;
	            default:
	                validRes = false;
	                System.out.println("Invalid resolution! Please try again.");
	        }

	    } while (!validRes);

	    System.out.print("Enter display size: ");
	    double size = input.nextDouble();

	    System.out.print("Enter quantity: ");
	    int qty = input.nextInt();

	    System.out.print("Enter price: ");
	    double price = input.nextDouble();
	    
		System.out.print("Enter item number: ");
	    int itemNum = input.nextInt();
	    
	    input.nextLine();

	    products[index] = new TV(name, itemNum, price, qty, screenType, resolution, size);
	    
	    System.out.println("TV added successfully!");
	}
	
	public static void addLaptop(Product[] products, Scanner input, int index) {

	    input.nextLine(); // clear buffer

	    System.out.print("Enter product name: ");
	    String name = input.nextLine();
		while (name.trim().isEmpty()) {
		    System.out.println("Error: Product name cannot be empty!");
		    System.out.print("Enter product name: ");
		    name = input.nextLine();
		}

	    System.out.print("Enter color: ");
	    String color = input.nextLine();
		while (color.trim().isEmpty()) {
		    System.out.println("Error: Color cannot be empty!");
		    System.out.print("Enter color: ");
		    color = input.nextLine();
		}
		
		String processor;
		do {
		    System.out.print("Enter processor (Intel Core/AMD Ryzen): ");
		    processor = input.nextLine();
		    
		    if (!processor.equalsIgnoreCase("Intel Core") && !processor.equalsIgnoreCase("AMD Ryzen")) {
		    	System.out.println("Invalid processor! Please enter Intel Core or AMD Ryzen.");
		    }
		    
		} while(!processor.equalsIgnoreCase("Intel Core") && !processor.equalsIgnoreCase("AMD Ryzen"));

	    String operatingSystem;
	    do {
	    	System.out.print("Enter operating system (Linux / Windows / IOS): ");
		    operatingSystem = input.nextLine();
		    
		    if (!operatingSystem.equalsIgnoreCase("Linux") && !operatingSystem.equalsIgnoreCase("Windows") &&
		    		!operatingSystem.equalsIgnoreCase("iOS")) {

		            System.out.println("Invalid OS! Please enter Linux, Windows, or iOS.");
		        }
	    	
	    }while(!operatingSystem.equalsIgnoreCase("Linux") && !operatingSystem.equalsIgnoreCase("Windows") &&
	    		!operatingSystem.equalsIgnoreCase("iOS"));

	    // storage size (validation loop)
	    int storageSize;
	    do {
	        System.out.print("Enter storage size (GB): ");
	        storageSize = input.nextInt();

	        if (storageSize <= 0) {
	            System.out.println("Invalid! Must be greater than 0.");
	        }

	    } while (storageSize <= 0);

	    input.nextLine(); // clear buffer


	    String storageType;
	    do {
	        System.out.print("Enter storage type (SSD / HDD): ");
	        storageType = input.nextLine().toUpperCase();

	        if (!storageType.equals("SSD") && !storageType.equals("HDD")) {
	            System.out.println("Invalid! Please enter SSD or HDD.");
	        }

	    } while (!storageType.equals("SSD") && !storageType.equals("HDD"));

	    System.out.print("Enter graphics card: ");
	    String graphicsCard = input.nextLine();
		while (graphicsCard.trim().isEmpty()) {
		    System.out.println("Error: Graphics card cannot be empty!");
		    System.out.print("Enter graphics card: ");
		    graphicsCard = input.nextLine();
		}

	    // RAM size (validation loop)
	    int ramSize;
	    do {
	        System.out.print("Enter RAM size (GB): ");
	        ramSize = input.nextInt();

	        if (ramSize <= 0) {
	            System.out.println("Invalid! Must be greater than 0.");
	        }

	    } while (ramSize <= 0);

	    // screen size (validation loop)
	    double screenSize;
	    do {
	        System.out.print("Enter screen size (inch): ");
	        screenSize = input.nextDouble();

	        if (screenSize <= 0) {
	            System.out.println("Invalid! Must be greater than 0.");
	        }

	    } while (screenSize <= 0);

	    System.out.print("Enter quantity: ");
	    int qty = input.nextInt();

	    System.out.print("Enter price: ");
	    double price = input.nextDouble();

	    System.out.print("Enter item number: ");
	    int itemNum = input.nextInt();

	    // create object
	    products[index] = new Laptop(name, itemNum, price, qty, color, processor, operatingSystem, storageSize, storageType
	    		, graphicsCard, ramSize, screenSize);
	    
	    System.out.println("Laptop added successfully!");
	}
	
	public static void addWashingMachine(Product[] products, Scanner input, int index) {

	    input.nextLine(); // clear buffer

	    System.out.print("Enter product name: ");
	    String name = input.nextLine();
		while (name.trim().isEmpty()) {
		    System.out.println("Error: Product name cannot be empty!");
		    System.out.print("Enter product name: ");
		    name = input.nextLine();
		}

	   
	    String loadType;
	    do {
	        System.out.print("Enter load type (Front Load / Top Load): ");
	        loadType = input.nextLine();

	        if (!loadType.equalsIgnoreCase("Front Load") &&
	            !loadType.equalsIgnoreCase("Top Load")) {

	            System.out.println("Invalid! Please enter Front Load or Top Load.");
	        }

	    } while (!loadType.equalsIgnoreCase("Front Load") &&
	             !loadType.equalsIgnoreCase("Top Load"));

	    
	    System.out.print("Enter capacity (kg): ");
	    int capacity = input.nextInt();

	   
	    int spinSpeed;
	    do {
	        System.out.print("Enter spin speed (RPM): ");
	        spinSpeed = input.nextInt();

	        if (spinSpeed <= 0) {
	            System.out.println("Invalid! Must be greater than 0.");
	        }

	    } while (spinSpeed <= 0);

	   
	    int energyRating;
	    do {
	        System.out.print("Enter energy rating (1-5): ");
	        energyRating = input.nextInt();

	        if (energyRating < 1 || energyRating > 5) {
	            System.out.println("Invalid! Enter value between 1 and 5.");
	        }

	    } while (energyRating < 1 || energyRating > 5);

	
	    System.out.print("Enter quantity: ");
	    int qty = input.nextInt();

	    System.out.print("Enter price: ");
	    double price = input.nextDouble();


	    System.out.print("Enter item number: ");
	    int itemNum = input.nextInt();


	    products[index] = new WashingMachine(name, itemNum, price, qty, loadType,capacity,spinSpeed, energyRating);

	    System.out.println("Washing Machine added successfully!");
	}
	
	public static void viewProduct(Product[] products) {
        System.out.println("\n======= Inventory List =======");
        boolean empty = true;
        for (int i = 0; i < products.length; i++) {
            if (products[i] != null) {
                System.out.println("Index [" + i + "]");
                System.out.println(products[i].toString());
                System.out.println("------------------------------------------------------");
                empty = false;
            }
        }
        if (empty) {
            System.out.println("The inventory is currently empty.");
        }
    }
	
	public static void addStock(Product[] products, Scanner sc) {
	    
	    boolean hasProducts = false;
	    for (Product p : products) {
	        if (p != null) {
	            hasProducts = true;
	            break;
	        }
	    }

	    if (!hasProducts) {
	        System.out.println("The inventory is empty. Please add products first.");
	        return;
	    }

	    viewProduct(products);
	    System.out.print("How many products do you want to add stock to? ");
	    int numProducts = sc.nextInt();

	    if (numProducts < 0) {
	        System.out.println("Invalid number. Must be 0 or above.");
	        return;
	    }

	    for (int i = 0; i < numProducts; i++) {
	        System.out.print("Enter the index of the product: ");
	        int index = sc.nextInt();
	        
	        if (index >= 0 && index < products.length && products[index] != null) {
	            System.out.print("Enter quantity to add: ");
	            int qty = sc.nextInt();
	            products[index].addStock(qty);
	        } else {
	            System.out.println("Invalid product index.");
	        }
	    }
	}

	public static void deductStock(Product[] products, Scanner sc) {
	    
	    boolean hasProducts = false;
	    for (Product p : products) {
	        if (p != null) {
	            hasProducts = true;
	            break;
	        }
	    }

	    if (!hasProducts) {
	        System.out.println("The inventory is empty. No products to deduct stock from.");
	        return;
	    }

	    viewProduct(products);
	    System.out.print("How many products do you want to deduct stock from? ");
	    int numProducts = sc.nextInt();

	    if (numProducts < 0) {
	        System.out.println("Invalid number. Must be 0 or above.");
	        return;
	    }

	    for (int i = 0; i < numProducts; i++) {
	        System.out.print("Enter the index of the product: ");
	        int index = sc.nextInt();
	        
	        if (index >= 0 && index < products.length && products[index] != null) {
	            System.out.print("Enter quantity to deduct: ");
	            int qty = sc.nextInt();
	            products[index].deductStock(qty);
	        } else {
	            System.out.println("Invalid product index.");
	        }
	    }
	}

	public static void setStatus(Product[] products, Scanner sc) {
	    
	    boolean hasProducts = false;
	    for (Product p : products) {
	        if (p != null) {
	            hasProducts = true;
	            break;
	        }
	    }

	    if (!hasProducts) {
	        System.out.println("The inventory is empty. No products to discontinue.");
	        return;
	    }

	    viewProduct(products);
	    System.out.print("Enter the index of the product to discontinue: ");
	    int index = sc.nextInt();

	    if (index >= 0 && index < products.length && products[index] != null) {
	        products[index].discontinueProduct();
	    } else {
	        System.out.println("Invalid product index.");
	    }
	}
	
}