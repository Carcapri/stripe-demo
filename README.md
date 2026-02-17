# Stripe Spring Boot E-Commerce Demo

A robust E-Commerce demonstration application built with **Java** and **Spring Boot**. This project showcases a real-world integration scenario combining **Stripe** for payment processing and **MongoDB** for data persistence.

## About The Project

For this project, we developed a web application that enables users to purchase fictional items from a catalog managed directly through **Stripe's Product Service**. While Stripe handles the entire payment lifecycle, all **PaymentIntents** and transaction records are stored in our local database and synchronized via the **Stripe API**.

Our primary goal is to demonstrate a real-world scenario that integrates Stripe with modern NoSQL databases like **MongoDB**. This combination is, in fact, a highly common and robust **architectural choice** in the industry today.

### Key Features

* **Dynamic Catalog:** Products are fetched directly from Stripe.
* **Secure Payments:** Full `PaymentIntent` lifecycle management.
* **Data Persistence:** Transaction history synced to MongoDB.
* **MVC Architecture:** Clean separation of concerns using Spring Boot.

## Built With

* [Java](https://www.java.com/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Stripe API](https://stripe.com/docs/api)
* [MongoDB](https://www.mongodb.com/)
* [Maven](https://maven.apache.org/)

## Getting Started

Follow these steps to get a local copy up and running.

### Prerequisites

* Java JDK (17 or higher recommended)
* [Maven](https://maven.apache.org/install.html) installed (or use the Maven Wrapper)
* A running instance of **MongoDB** (Local or Atlas)
* A **Stripe Account** with API Keys

### Installation

1. **Clone the repository**

   ```bash
   git clone [https://github.com/Carcapri/stripe-demo.git](https://github.com/Carcapri/stripe-demo.git)
   cd stripe-demo
   ```

2. **Configuration (.env)**

   Create a `.env` file in the **root** of the project directory. Copy and paste the following format, filling in your specific credentials:

   ```properties
   # Stripe Keys (Found in Stripe Dashboard -> Developers -> API Keys)
   STRIPE_PUBLIC_KEY=pk_test_...
   STRIPE_SECRET_KEY=sk_test_...

   # Database Connection
   MONGODB_URI=mongodb+srv://...
   ```

   **Note:** Ensure your MongoDB instance is running before starting the application.

3. **Run the Application**

   You can run the application using the Maven plugin:

   ```bash
   mvn spring-boot:run
   ```

## Usage

Once the server is running, you can access the storefront in your browser:

**URL:** `http://localhost:8082/tienda/`

1. Browse the catalog (loaded from your Stripe Dashboard).
2. Select an item and proceed to checkout.
3. Fill in the payment form.

### Testing Payments

Since this project allows for testing mode, you do not need a real credit card. You can use **Stripe's Test Cards** to simulate various scenarios (success, decline, etc.).

* **Card Number:** `4242 4242 4242 4242`
* **Expiry:** Any future date (e.g., `12/30`)
* **CVC:** Any 3 digits (e.g., `123`)
* **ZIP:** Any valid zip code (e.g., `12345`)

For a full list of test card numbers (including error simulations), visit the official [Stripe Testing Documentation](https://stripe.com/docs/testing).

## Result

If the payment information is correct:

1. The `PaymentIntent` will be fulfilled on Stripe.
2. The transaction status will be reflected in your **MongoDB** database.
3. The user will see a success confirmation screen.
