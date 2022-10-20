# SpringBoot-REST-project

It was created for learning Spring, Hibernate, JWT authentication.

## About

This is a SpringBoot project which lets you register as a new user and then log in with your account and add your own notes. In this project I used postgres database before launching you need to create your database and you can change settings at `application.properties` file.

### User registration, login Requests

Firstly you need to register your account:

`POST @ localhost:8080/api/auth/signup` with body:
```
{
    "username":"test",
    "email":"test@gmail.com",
    "password":"test"
}
```

Then you can log in and after log in you receive jwt token, refresh token and token expiration time:

`POST @ localhost:8080/api/auth/login` with body:
```
{
    "username":"test",
    "password":"test"
}
```
And response is:
```
{
    "token": "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0OCIsImV4cCI6MTYxNDQ1NjI0OH0...",
    "refreshToken": "e6725b78-350d-40c4-99d4-4f6ca5612928",
    "expiresAt": "2021-02-27T20:04:08.838567Z"
}
```
Refresh Token is used when given JWT token expires it is valid for 15 minutes. To get new valid JWT token use:

`POST @ localhost:8080/api/auth/refreshtoken` with body:
```
{
    "refreshToken":"e6725b78-350d-40c4-99d4-4f6ca5612928",
    "username":"test"
}
```
For every bookmark request you need to add your JWT token to authorization header as Bearer token.

![alt text](https://i.imgur.com/YSQMtAJ.png)

Also you can logout and delete your refresh token so that after valid JWT token expires you wont be able to refresh it:

`POST @ localhost:8080/api/auth/logout` with body:
```
{
    "refreshToken":"e6725b78-350d-40c4-99d4-4f6ca5612928",
    "username":"test"
}
```
### Notes Requests

To add bookmark as a logged in user:

`POST @ localhost:8080/api/bookmark/add` with body:
```
{
    "title":"note2",
    "description":"note2"
}
```
To get all added notes:

`GET @ localhost:8080/api/bookmark`

To delete bookmark:

`DELETE @ localhost:8080/api/bookmark/delete/{id}`

You can only see notes that you added for your logged in user. If you login with other account you wont be able to see them or delete them if you didn't add them. If something wont work there will be error message with some details about error.


