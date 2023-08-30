## Restful state machine

This repository contains a RESTful application that leverages Spring State Machine to process product categories and generate CSV files based on the received data. The application also supports generating ZIP archives of product files and sending email notifications upon completion.

### Features

1. Start Spring State Machine to process product categories:
    - On receiving a POST request to `/api/start`, the application triggers Spring State Machine to process product categories.
    - Categories are fetched from `https://dummyjson.com/products/categories` and arranged alphabetically in a queue.

2. Generate Product Files:
    - The application fetches product data for each category from sources like `https://dummyjson.com/products/category/smartphones` and saves the data as CSV files (e.g., `smartphones.csv`).
    - Product entities have fields: id, title, description, price, discountPercentage, rating, stock, brand, category.
    - Files are saved in the directory `resource/result` based on the brand of the product.

3. Check and Process Queue:
    - The application checks if the category queue is empty; if not, it processes the next category.

4. Create Brand Archives:
    - After generating files for each brand, the application creates ZIP archives for each brand (e.g., `Apple.zip`) containing the corresponding product files.

5. Send Email Notifications:
    - Upon completing archive creation, the application sends an email to specified recipients.
    - Email subject: "Product archives generated"
    - Email body: List of generated archives and files.

6. Custom Category File Generation:
    - Generate files for a specific category by sending a POST request to `/api/start?category=smartphones`.

7. Get Information on File Generation:
    - Retrieve information about all file generation processes, including start time, generated archives, and email send time, using a GET request.

8. Optional Archive Generation without Email:
    - Enable archive generation without sending emails by using the parameter `send=false` in the request.

### Prerequisites

1. Spring Boot
2. Gradle
3. Spring State Machine
4. Docker (for database)
5. MongoDB (database)
6. Java 8 or higher
7. Spring email
8. Internet connection (for fetching product data)

### Getting Started

1. Clone this repository:

   ```sh
   git clone <repository-url>
   ```

2. Give script execution permissions
   ```sh
   chmod +x run.sh
   ```

3. Run script
   ```sh
   ./run.sh
   ```

### State machine diagram

![](/uml/StateMachineUML.png)


### Usage

1. To start processing product categories, make a POST request to `/api/start`.

2. For custom category file generation, make a POST request to `/api/start?category=smartphones&sendEmail=false`.

3. To retrieve information about file generation processes, use a GET request to `/api/info`.

4. Persist current running state machine, make POST request to `/api/state`

### Testing

1. Unit tests are provided for testing functionality.
2. Run tests using Gradle: `./gradlew test`
