📈 bc-xfin-service
A Spring Boot microservice for managing and visualizing stock OHLCV data, integrated with Redis and PostgreSQL. Designed to power real-time financial charts using data fetched from Yahoo Finance.

📦 Features
✅ Stores OHLCV (Open, High, Low, Close, Volume) data in PostgreSQL

🚀 Caches real-time data in Redis for fast access

📅 Scheduled fetching and validation of stock data

🔒 Recovery API protected by a secret token

📊 Integrates with frontend to render charts using real stock data

🔧 Tech Stack
Java 17

Spring Boot

PostgreSQL

Redis

Maven

Thymeleaf (via frontend bc-xfin-web)

Deployed on AWS EC2

🔐 Environment Variables
This project uses a .env file to load environment variables securely. The actual file is excluded from version control (.gitignore), but a sample file is provided.

1. Create and Configure .env
bash
Copy
Edit
cp .env.example .env
2. Fill in values:
Variable	Description
DB_HOST	PostgreSQL host/IP
DB_NAME	Name of the PostgreSQL database
DB_USER	Database username
DB_PASS	Database password
REDIS_HOST	Redis server host
REDIS_PORT	Redis server port (default: 6379)
RECOVERY_SECRET	Secret token to authorize recovery
🛠️ Build & Run
💻 Local Development
Ensure PostgreSQL and Redis are running (or connect to remote services)

Create .env file (as shown above)

Build the app:

bash
Copy
Edit
export $(cat .env | xargs) && mvn clean package
Alternatively, you can install dotenv-cli for better cross-platform support.

Run the app:

bash
Copy
Edit
java -jar target/bc-xfin-service-0.0.1-SNAPSHOT.jar
App runs at: http://localhost:8101

🚀 Deploying to AWS EC2
1. SSH into your instance
bash
Copy
Edit
ssh ubuntu@your-ec2-public-ip
2. Upload .jar file
bash
Copy
Edit
scp target/bc-xfin-service-0.0.1-SNAPSHOT.jar ubuntu@your-ec2-public-ip:~/
3. Upload .env file (do NOT commit it)
bash
Copy
Edit
scp .env ubuntu@your-ec2-public-ip:~/
4. Run the app on EC2
bash
Copy
Edit
export $(cat .env | xargs) && java -jar bc-xfin-service-0.0.1-SNAPSHOT.jar
📊 API Endpoints (Sample)
GET /api/ohlcv?symbol=AAPL&start=...&end=... – Fetch OHLCV data

POST /api/recover/ohlcv – Trigger OHLCV data recovery (requires Recovery-Token header)

⚠️ Notes
Data validation is enforced at startup. If corrupted or missing data is detected, the app will halt.

Recovery is manual or via secure API call using your configured token.

📁 Repository Structure
css
Copy
Edit
├── src/main/java/com/bootcamp/bc_xfin_service
│   ├── controller
│   ├── entity
│   ├── repository
│   ├── scheduler
│   ├── service
│   └── ...
├── src/main/resources
│   ├── application.yml
│   └── ...
├── .env.example
├── .gitignore
├── pom.xml
└── README.md
