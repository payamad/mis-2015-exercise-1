package mmbuw.com.brokenproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class BrokenActivity extends Activity {

    private EditText auntEdith;
    public final static String EXTRA_MESSAGE = "mmbuw.com.brokenproject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broken);
        auntEdith = (EditText)findViewById(R.id.edittext);
        /*Second Error was definition of auntEdit. It has to be a private property of Broken activity not a local variable in onCreate method.
        * So I change the variable name to auntEdith to fix this error.*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.broken, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /*Lack of View parameter in definition of brokenFunction was the third error. */
    public void brokenFunction(View view){
        //I was once, perhaps, possibly a functioning function
        if (auntEdith.getText().toString().equals("Timmy")){
            System.out.println("Timmy fixed a bug!");
        }

        System.out.println("If this appears in your console, you fixed a bug.");
        Intent intent = new Intent(this,AnotherBrokenActivity.class);
        String message = "This string  will be passed to the new activity";
        startActivity(intent);
    }
}
