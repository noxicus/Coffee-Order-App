/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/
package com.example.justjava;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.justjava.App.CHANEL_1_ID;


/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    public int quantity = 0;
    public int price = 5;
    boolean hasWhippedCream = false;
    boolean hasChocolate = false;
    String name;
    NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = NotificationManagerCompat.from(this);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        // Check for whipped cream
        CheckBox checkBoxWhippedCream = findViewById(R.id.whipped_cream_checkbox);
        hasWhippedCream = checkBoxWhippedCream.isChecked();

        // Check for chocolate
        CheckBox checkBoxChocolate = findViewById(R.id.chocolate_checkbox);
        hasChocolate = checkBoxChocolate.isChecked();

        // Get input name
        EditText nameBox = findViewById(R.id.name_view);
        name = nameBox.getText().toString();

        // Calculate price
        int price = calculatePrice();

        // Display order summary
        displayMessage(createOrderSummary(price, hasWhippedCream, hasChocolate, name));

        // Creates email intent
        /*Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Coffee order for " + name);
        intent.putExtra(Intent.EXTRA_TEXT, price);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    /**
     * Method for showing notification
     */
    public void showNotification(View v) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANEL_1_ID)
                .setSmallIcon(R.drawable.baseline_flash_on_black_24dp)
                .setContentTitle(getString(R.string.notTitle))
                .setContentText(getString(R.string.notContent))
                .setPriority(NotificationCompat.PRIORITY_MAX);

        notificationManager.notify(1, builder.build());
    }

    /**
     * This method increments quantity
     */
    public void increment(View view) {
        if (quantity >= 100) {
            Toast.makeText(this, "Can't order more then 100 cups", Toast.LENGTH_LONG).show();
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method decrements quantity
     */
    public void decrement(View view) {
        if (quantity == 1) {
            Toast toast = Toast.makeText(this, "Can't order less then 1 cup", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * Method for calculating price
     *
     * @return total price of all topping + quantity * price of coffee
     */
    public int calculatePrice() {
        int whippedCream = 1;
        int chocolate = 2;
        int totalPrice = 0;
        if (hasWhippedCream && hasChocolate) {
            totalPrice = (price + whippedCream + chocolate) * quantity;
        } else if (hasWhippedCream) {
            totalPrice = (price + whippedCream) * quantity;
        } else if (hasChocolate) {
            totalPrice = (price + chocolate) * quantity;
        } else
            totalPrice = price * quantity;

        return totalPrice;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView priceTextView = (TextView) findViewById(R.id.order_summary_text_view);
        priceTextView.setText(message);
    }

    /**
     * Create a string order summary
     *
     * @param price of the order
     * @param name  string from input field
     * @return order summary
     */
    @SuppressLint("StringFormatInvalid")
    private String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolate, String name) {
        String message = getString(R.string.order_summary_name, name);
        message += "\n" + getString(R.string.order_summary_whipped, hasWhippedCream);
        message += "\n" + getString(R.string.order_summary_chocolate, hasChocolate);
        message += "\n" + getString(R.string.order_summary_quantity, quantity);
        message += "\n" + getString(R.string.order_summary_total, price);
        message += "\n" + getString(R.string.thank_you);
        return message;
    }
}