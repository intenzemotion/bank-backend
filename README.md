# Banking App

This is a simple banking app that can perform user registration, login, credit, debit, transferring between two accounts, inquiring account name and balance, mailing feature, and generating bank statement.

## Project Structure

![image](https://github.com/intenzemotion/bank-backend/assets/6555771/e1a44d48-5f51-49f9-802f-ebf43f4e44f6)

## API Structure

![image](https://github.com/intenzemotion/bank-backend/assets/6555771/806dd4fe-79b6-4c8c-91f7-001b902b0a27)

## Prerequisite

1. Install [JDK](https://www.oracle.com/java/technologies/downloads/)
2. Install [Maven](https://maven.apache.org/download.cgi)
3. Install [MySQL Community](https://dev.mysql.com/downloads/installer/)

## How to Run

1. **Clone the Repository:**
   
    ```bash
    git clone https://github.com/intenzemotion/bank-backend.git
    ```
    
2. **Navigate to the project directory:**
   
    ```bash
    cd bank-backend
    ```
    
3. **Build the Project:**
   
    ```bash
    mvn clean install
    ```
    
4. **Create a Database Schema**
   
   **Important**: In MySQL Workbench, please create a new schema `maybank_db` inside your local instance. You may need to change your credential in `\src\main\resources\application.properties` to adhere with your own.
   
   Current config:
   - spring.datasource.username=root
   - spring.datasource.password=root

6. **Run the Application:**
   
    ```bash
    mvn spring-boot:run
    ```
    
    **Note**: All tables will be automatically created during first run.

## How to test using Curl

- Make sure the application is running before executing the following `curl` commands.
- Replace `<token>` with your user token obtained during login.
- Replace `<your-account-number>` with the account number for the credit or debit operation.
- You can copy and paste these `curl` commands straight into your Postman URL box and Postman will automatically handle it.

### 1. Register

```bash
curl -L 'http://localhost:8080/api/user/register' -H 'Content-Type: application/json' --data-raw '
{
  "firstName": "Wal Ikram",
  "lastName": "Suaimi",
  "gender": "Male",
  "email": "wsuaimi@gmail.com",
  "password": "abc123",
  "address": "123 Cedar Street, Villagetown",
  "phoneNumber": "+60 9876 54321"
}
'
```

### 2. Login

```bash
curl -L 'http://localhost:8080/api/user/login' -H 'Content-Type: application/json' --data-raw '
{
    "email": "wsuaimi@gmail.com",
    "password": "abc123"
}
'
```

### 3. Credit
```bash
curl -L 'http://localhost:8080/api/user/credit' -H 'Authorization: Bearer <token>' -H 'Content-Type: application/json' -d '
{
    "accountNumber": "<your-account-number>",
    "amount": 10000
}
'
```

### 4. Debit
```bash
curl -L 'http://localhost:8080/api/user/debit' -H 'Authorization: Bearer <token>' -H 'Content-Type: application/json' -d '
{
    "accountNumber": "<your-account-number>",
    "amount": 500
}
'
```

### 5. Name Enquiry
```bash
curl -L -X GET 'http://localhost:8080/api/user/nameEnquiry' -H 'Authorization: Bearer <token>' -H 'Content-Type: application/json' -d '
{
    "accountNumber": "<your-account-number>"
}
'
```

### 6. Balance Enquiry
```bash
curl -L -X GET 'http://localhost:8080/api/user/balanceEnquiry' -H 'Authorization: Bearer <token>' -H 'Content-Type: application/json' -d '
{
    "accountNumber": "<your-account-number>"
}
'
```

### 7. Transfer
```bash
curl -L 'http://localhost:8080/api/user/transfer' -H 'Authorization: Bearer <token>' -H 'Content-Type: application/json' -d '
{
    "sourceAccountNumber": "<your-account-number>",
    "destinationAccountNumber": "<another-account-number>",
    "amount": 2500
}
'
```

### 8. Generate Bank Statement
```bash
curl -L 'http://localhost:8080/api/bankStatement?accountNumber=<your-account-number>&startDate=<start-date>&endDate=<end-date>' -H 'Authorization: Bearer <token>'
```

**Note**: Generate Bank Statement uses parameter instead of request body, please make sure to replace these params:
- `<your-account-number>` with your auto-generated account number during user registration.
- `<start-date>` with starting date in the format of YYYY-MM-DD. Example: `2024-01-20`
- `<end-date>` with end date in the format of YYYY-MM-DD. Example: `2024-01-25`
- `<token>` with your user token obtained during login.
- If you want to filter only by today's date, both `<start-date>` and `<end-date>` will be the same date.

## Potential Improvements

- **Enhanced Validation:** Improve input validation to handle edge cases and provide more informative error messages.
- **Unit Testing:** Expand unit test coverage for both service and controller layers to ensure robustness.
- **Logging:** Implement comprehensive logging to aid in debugging and monitoring.

## Additional Notes

This project is far from complete. I am planning to build the front-end for this app so end user can interact with it while expanding important modules for a banking app such as:
- Bill payments
- Account management
- Currency Conversion
- Alerts and notification
- Two-factor authentication
- Budget and spending analysis

## Contribution

If you'd like to contribute, feel free to fork the repository and submit a pull request. Please follow the coding standards and include appropriate tests.

## Issues

If you encounter any issues or have suggestions, please open an issue on the GitHub repository.

## License

This project is licensed under the [MIT License](LICENSE).
