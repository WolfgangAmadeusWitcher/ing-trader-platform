# ing-trader-platform

- We have four test users under the platform, one is admin and the other ones are standard Users.

  The credentials are as follows;

- Admin User
  - Username: admin
  - Password: admin
- Test User 1
  - Username: ingHubTestTraderOne
  - Password: testUser123
- Test User 2
  - Username: ingHubTestTraderTwo
  - Password: testUser123
- Test User 3
  - Username: ingHubTestTraderThree
  - Password: testUser123

The application is working with JWT, therefore you need to login with one of these users before interacting with the system. 

You can access swagger page to see API endpoints from it's default address: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

You can interact with the Endpoints via Postman.
You can login to the system from http://localhost:8080/api/auth/login with one the credentials as follows:

{
    "username":"admin",
    "password":"admin"
}

<img width="675" alt="image" src="https://github.com/user-attachments/assets/ca8e5c06-467b-4a2c-a0e3-247b7a9b1666" />

Afterwards you will receive your JWT from the login endpoint

<img width="679" alt="image" src="https://github.com/user-attachments/assets/71d9b243-5976-4d08-b495-822e846ace0b" />

With that JWT you will be able to interact with the system as follows 

<img width="634" alt="image" src="https://github.com/user-attachments/assets/598110bc-219b-4f4e-8e43-3525ecb91ad2" />

You will be able to reach every endpoint in the system with this fashion. 

There are also dummy Order and Asset datas for the Users as follows, you can use them for your initial tests.

Test Orders

<img width="508" alt="image" src="https://github.com/user-attachments/assets/68c95e86-b461-4086-b212-931c329287df" />

Test Assets

<img width="348" alt="image" src="https://github.com/user-attachments/assets/91048e6d-46dc-4cfd-aa18-20e0c14fa088" />

Test Users

<img width="509" alt="image" src="https://github.com/user-attachments/assets/b69427f1-1f89-437f-bf6f-e6cfa4aa2b8d" />

Certain endpoints are only accessable by Admin users as follows;

- The endpoint below matches the orders
<img width="916" alt="image" src="https://github.com/user-attachments/assets/231abb7b-8dcd-4512-b283-a7ec6bac67e4" />

- The endpoint below lists all users registered in the system
<img width="914" alt="image" src="https://github.com/user-attachments/assets/0b7e17b9-e7fa-4b3f-907a-1fc4ade67733" />

So if you want to interact with the endpoints above, just make sure you access with the Admin token. 






