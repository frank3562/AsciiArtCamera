/*
Programmer: Franklin Adams
Date: 3/5/2017
Program Name: RL2ASCII
Function: This program will take a picture acessing the camera functions of the phone and turn the
result into ASCII text art. The user will select the "CAPTURE REAL LIFE" button, the phone will
access the camera function, the user will take photo and be given the option to accept or cancel
the current photo. The User will then be able to see a image thumbnail in upper left corner and
choose the second button "CONVERT 2 ASCII", the phone will process the image with a progress bar
action while waiting. The image will precent itself as a textart in a string format below the 2 main
buttons. The user can then choose to take another photo to process by hitting the "CAPTURE REAL LIFE"
button again, or close the program.
 */

package as.frank3562.rl2ascii;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RL2ASCIIActivity extends AppCompatActivity
{
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 100;

    private Bitmap capturedPhoto;       //Variable used for camera captured image object
    private TextView asciiImage;        //Variable used for ascii Art TextView object
    private Button asciiBtn,            //Variable used for "CONVERT 2 ASCII" Button object
            cameraBtn;                  //Variable used for "CAPTURE REAL LIFE" Button object
    private ProgressBar asciiPB;        //Variable used for ProgressBar object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rl2_ascii);

        //Connect the artTextView TextView object
        asciiImage = (TextView) findViewById(R.id.artTextView);

        //Connect the "CONVERT 2 ASCII" Button object and set default to disabled
        asciiBtn = (Button) findViewById(R.id.asciiButton);
        asciiBtn.setEnabled(false);

        //Connect the "CAPTURE REAL LIFE" Button object
        cameraBtn = (Button) findViewById(R.id.cameraButton);

        //Connect the asciiProgressBar ProgressBar object and hide it from view
        asciiPB = (ProgressBar) findViewById(R.id.asciiProgressBar);
        asciiPB.setAlpha(0); //sets the progress bar to be invisible, set to 1 to show

    }//end of OnCreate

    public void toCamera(View view) {
        //Initiate and/or clear the image in artTextView
        asciiImage.setText("");

        //Create intent for image capture from camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);

    }//end of toCamera

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Connect the cameraImageView ImageView object
        ImageView photoPreview = (ImageView) findViewById(R.id.cameraImageView);

        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //Set up the captured image preview
                Bundle extras = data.getExtras();
                capturedPhoto = (Bitmap) extras.get("data");
                photoPreview.setImageBitmap(capturedPhoto);

                //Turn on the "CONVERT 2 ASCII" Button object
                asciiBtn.setEnabled(true);

            }//end of if resultCode == RESULT_OK
            else if (resultCode == RESULT_CANCELED) {
                //Produce Image Captured Canceled messege
                Toast.makeText(this, "Canceled Real Life Capture", Toast.LENGTH_SHORT).show();

            }//end of else if resultCode == RESULT_CANCELED
            else {
                //Produce Image Captured Failed messege
                Toast.makeText(this, "Real Life Capture Failed", Toast.LENGTH_SHORT).show();

            }//end of else if resultCode == RESULT_CANCELED

        }//end of if requestCode == CAPTURE_IMAGE_REQUEST_CODE

    }//end of onActivityResult

    public void toAscii(View view) {
        new PerformAsyncTask().execute();
    }//end of toAscii

    private class PerformAsyncTask extends AsyncTask<Void, Void, Void> {
        //Create String variable to hold ascii Art
        String asciiArtWork;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Turn off both Button objects
            asciiBtn.setEnabled(false);
            cameraBtn.setEnabled(false);

            //Show ProgressBar object
            asciiPB.setAlpha(1);

            //Set the current asciiArtWork TextView to newline
            asciiArtWork = "\n";

        }//end of onPreExecute

        @Override
        protected Void doInBackground(Void... params)
        {
            int thumbnailWidth = capturedPhoto.getWidth(),
                thumbnailHeight = capturedPhoto.getHeight(),
                scaleWidth = 2,
                scaleHeight = 2;

            for (int y = 0; y < thumbnailHeight / scaleHeight; y++) {
                for (int x = 0; x < thumbnailWidth / scaleWidth; x++) {
                    //Scale width and height of the capturedPhoto
                    int pixel = capturedPhoto.getPixel(x * scaleWidth, y * scaleHeight);

                    //Get the number value of the pixels from the RGB scale
                    int redVal = Color.red(pixel),
                        greenVal = Color.green(pixel),
                        blueVal = Color.blue(pixel);

                    //Convert RGB scale to Gray scale
                    int grayVal = (redVal + greenVal + blueVal) / 3;

                    if( grayVal < 35 )
                        asciiArtWork += "MM";
                    else if( grayVal <= 52 )
                        asciiArtWork += "$$";
                    else if( grayVal <= 69 )
                        asciiArtWork += "##";
                    else if( grayVal <= 86 )
                        asciiArtWork += "%%";
                    else if( grayVal <= 103 )
                        asciiArtWork += "**";
                    else if( grayVal <= 120 )
                        asciiArtWork += "++";
                    else if( grayVal <= 137 )
                        asciiArtWork += "vV";
                    else if( grayVal <= 154 )
                        asciiArtWork += "-;";
                    else if( grayVal <= 171 )
                        asciiArtWork += "--";
                    else if( grayVal <= 188 )
                        asciiArtWork += ";;";
                    else if( grayVal <= 205 )
                        asciiArtWork += "::";
                    else if( grayVal <= 222 )
                        asciiArtWork += "..";
                    else
                        asciiArtWork += "  ";

                }//end of for int x

                //Add a newline char at the current end of asciiArtWork string
                asciiArtWork += "\n";

            }//end of for int y

            return null;
        }//end of doInBackground

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            //Turn on "CAPTURE REAL LIFE" Button object
            cameraBtn.setEnabled(true);

            //Turn off "CONVERT 2 ASCII" Button object
            asciiBtn.setEnabled(false);

            //Hide ProgressBar object
            asciiPB.setAlpha(0);

            //Set asciiImage ImageView to show your ASCII art found in asciiArtWork String
            asciiImage.setText(asciiArtWork);

        }//end of onPostExecute

    }//end of PerformAsyncTask

}//end of RL2ASCIIActivity




























