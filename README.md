![](book_picture.png)

# BookStore App

<hr>

## Short description

This is an online application that was created to help users find and purchase their favorite books online. 
You can choose a book by author and price, add it to the shopping cart and place an order

<hr>

## Technologies and tools used

* Java (SOLID, Stream Api, Collections)
* Hibernate
* Spring (Boot, Data JPA, Web MVC, Security)
* Test: JUnit, Mockito, Test Containers
* Documentation: Swagger
* Version Control: Git
* Maven
* Tomcat
* Docker

<hr>

## Functionalities of controllers

#### Authentication controller

>  POST method: /api/auth/registration

This endpoint is for registration new user. Example of request body:

```json
{
  "email":"user@gmail.com",
  "password":"1212user",
  "repeatPassword":"1212user",
  "firstName":"User",
  "lastName":"SomeUser",
  "shippingAddress":"Skovorody street, 100"
}
```

When registration will be successful, you will get response body:

```json
{
  "id":1,
  "email":"user@gmail.com",
  "firstName":"User",
  "lastName":"SomeUser",
  "shippingAddress":"Skovorody street, 100"
}
```
<br>

>  POST method: /api/auth/login

This endpoint is for login user to app. Example of request body:

```json
{
  "email": "user@gmail.com",
  "password": "1212user"
}
```
When email and password are correct, you will get response token:

```json
{
  "token":"eyJhbGciOiJIUzM4NCJ9.eyJzd......"
}
```

<br>

#### Book controller

>  GET method: /api/books

This endpoint shows to user all available books in the store. Example of response body:

```json
[
{
  "id": 1,
  "title": "Some book",
  "author": "Author",
  "isbn": "1232222132",
  "price": 25,
  "categoryIds": [
    1
  ],
  "description": "description",
  "coverImage": "https://image.com/cover-image.jpg"
},
{
"id": 2,
"title": "Some Book2",
"author": "Author2",
"isbn": "1232222132332",
"price": 35,
"categoryIds": [
  2
],
"description": "description",
"coverImage": "https://image.com/cover-image2.jpg"
}
]
```

<br>

>  GET method: /api/books/{id}

This endpoint shows to user special book. Example of response body:

```json
{
  "id": 3,
  "title": "Another Book",
  "author": "Author",
  "isbn": "1232222132522",
  "price": 25,
  "categoryIds": [
    1
  ],
  "description": "description",
  "coverImage": "https://image.com/cover-image3.jpg"
}
```

<br>

>  GET method: /api/books/search

This endpoint shows to user books, filtered by authors and price.

<br>

>  POST method: /api/books

This endpoint is for ADMIN, to add book to the store. Example of request body:

```json
{
  "title":"Some book",
  "author":"Some author",
  "isbn":"1234332qsd",
  "price": 40,
  "categories": [1],
  "description":"some description",
  "coverImage":"https://image.com/cover-image3.jpg"
}
```
<br>

>  PUT method: /api/books/{id}

This endpoint is for ADMIN, to update existing specific book. Request body is the same with previous endpoint.

<br>

>  DELETE method: /api/books/{id}

This endpoint is for ADMIN, to delete specific book.

<br>

#### Category controller

>  GET method: /api/categories

This endpoint shows to user all available categories of books. Example of response body:

```json
[
  {
    "id": 1,
    "name": "Drama",
    "description": "no description"
  },
  {
    "id": 2,
    "name": "Classic",
    "description": "no description"
  }
]
```

<br>

>  GET method: /api/categories/{id}

This endpoint shows to user special category. Example of response body:

```json
{
  "id": 1,
  "name": "Drama",
  "description": "no description"
}
```

<br>

>  GET method: /api/categories/{id}/books

This endpoint shows to user all books by special category. Example of response body:

```json
[
  {
    "id": 3,
    "title": "Another Book",
    "author": "Author",
    "isbn": "1232222132522",
    "price": 25,
    "description": "some description",
    "coverImage": "https://image.com/cover-image3.jpg"
  },
  {
    "id": 5,
    "title": "Another Book3",
    "author": "Author",
    "isbn": "1232222132344",
    "price": 25,
    "description": "some description",
    "coverImage": "https://image.com/cover-image5.jpg"
  }
]
```

<br>

>  POST method: /api/categories

This endpoint is for ADMIN, to add category to the store. Example of request body:

```json
{
  "name": "Classic",
  "description": "no description"
}
```
<br>

>  PUT method: /api/categories/{id}

This endpoint is for ADMIN, to update existing specific category. Request body is the same with previous endpoint.

<br>

>  DELETE method: /api/categories/{id}

This endpoint is for ADMIN, to delete specific category.

<br>

#### ShoppingCart controller

>  GET method: /api/cart

This endpoint shows user's shopping cart with cart items. Example of response body:

```json
{
  "id": 1,
  "userId": 2,
  "cartItems": [
    {
      "id": 2,
      "bookId": 4,
      "bookTitle": "Another Book2",
      "quantity": 1
    },
    {
      "id": 1,
      "bookId": 5,
      "bookTitle": "Another Book3",
      "quantity": 1
    }
  ]
}
```

<br>

>  POST method: /api/cart

This endpoint is for user to add book to shopping cart. Example of request body:

```json
{
  "bookId": 4,
  "quantity": 1
}
```

<br>

>  PUT method: /api/cart/cart-items/{id}

This endpoint is for user to update book quantity of the cart item.

<br>

>  DELETE method: /api/cart/cart-items/{id}

This endpoint is for user to delete cart item from shopping cart.

<br>

#### Order controller

>  GET method: /api/orders

This endpoint shows user's order history. Example of response body:

```json
[
  {
    "id": 1,
    "userId": 2,
    "orderItems": [
      {
        "id": 1,
        "bookId": 4,
        "quantity": 1
      },
      {
        "id": 2,
        "bookId": 5,
        "quantity": 7
      }
    ],
    "orderDate": "2024-01-10T14:46:59",
    "total": 200,
    "status": "PENDING"
  }
]
```

<br>

>  POST method: /api/orders

This endpoint is for user to form order from shopping cart. Example of request body:

```json
{
  "shippingAddress":"Some address, 180"
}
```

<br>

>  GET method: /api/orders/{id}/items

This endpoint shows specific order. Example of response body:

```json
[
{
"id": 1,
"bookId": 4,
"quantity": 1
},
{
"id": 2,
"bookId": 5,
"quantity": 7
}
]
```

<br>


>  GET method: /api/orders/{orderId}/items/{itemId}

This endpoint shows specific order item of the order. Example of response body:

```json
{
"id": 2,
"bookId": 5,
"quantity": 7
}
```

<br>


>  PATCH method: /api/orders/{id}

This endpoint is for ADMIN, to update order status.

<hr>

## Short about the difficulties on the way to implementation

The first difficulties were with understanding the project architecture, 
connections, and responsibilities of entities. But over time everything became clear. 
The next difficult step was Liquibase, namely the correctness of the changesets writing, 
its syntax, but again, a little research and time paid off. 
In the final stage of the project, there were difficulties with writing tests, 
because I met SpringBoot testing for the first time and testing different layers 
of the program required different approaches. But this was also overcome 🤟

