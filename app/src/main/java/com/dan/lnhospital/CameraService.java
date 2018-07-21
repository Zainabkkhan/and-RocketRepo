package com.dan.lnhospital;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;




import com.dan.lnhospital.AppVar.AppVariable;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceView;


public class CameraService extends Service
{
      //Camera variables
      //a surface holder
      
      
      //a variable to control the camera
      private Camera mCamera;
      //the camera parameters
      private Parameters parameters;
      private int cameraId = 0;
      int serverResponseCode;
      String Username;
      /** Called when the activity is first created. */
     
    @Override
    public void onCreate()
    { 
    	
    	
        super.onCreate();
         
    }
    @Override
    public void onStart(Intent intent, int startId) {
      // TODO Auto-generated method stub
      super.onStart(intent, startId);
      
      Bundle bundle = intent.getExtras();
	  
	Username=  bundle.getString("user");
      AutoImageCapture();
     
    }

     
     
    Camera.PictureCallback mCall = new Camera.PictureCallback()
    {
    	
    	@Override
       public void onPictureTaken(byte[] data, Camera camera)
       {
    		System.out.println("1");  
             //decode the data obtained by the camera into a Bitmap
    	   Log.i("LNJP.Camera", "before direcory");
    	   System.out.println("1");
    	   File pictureFileDir = getDir();
    	   Log.i("LNJP.Camera", "Camera on Picture taken");
		    if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

		      Log.d("LNJP.Camera", "Can't create directory to save image.");
		      //Toast.makeText(this, "Can't create directory to save image.",Toast.LENGTH_LONG).show();
		      return;

		    }

		  
		    long unixTime = System.currentTimeMillis() / 1000L;
		    
		    String photoFile = AppVariable.getUsername()+"-"+unixTime+".jpg";
       
		  String filename = pictureFileDir.getPath() + File.separator +photoFile;

		    File pictureFile = new File(filename);

		    try {
		      FileOutputStream fos = new FileOutputStream(pictureFile);
		      fos.write(data);
		      fos.close();
		      /*if(mCamera!=null){
		            mCamera.stopPreview();
		            mCamera.setPreviewCallback(null);
		            mCamera.release();
		            mCamera = null;
		        }*/
		      mCamera.release();
		   new UploadWebPageTask(filename,photoFile).execute();
		    
		      
		      Log.i("LNJP.Camera", "Camera image is saving");
		      
		      
		    
		      //Toast.makeText(context, "New Image saved:" + photoFile,Toast.LENGTH_LONG).show();
		    } catch (Exception error) {
		      Log.d("LNJP.Camera", "File" + filename + "not saved: "
		          + error.getMessage());
		     // Toast.makeText(context, "Image could not be saved.",Toast.LENGTH_LONG).show();
		    }
     
       }
    };


      @Override
      public IBinder onBind(Intent intent) {
            // TODO Auto-generated method stub
            return null;
      }
      private void AutoImageCapture(){
      	
      	
      		if (!getPackageManager()
      	        .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
      			Log.i("LNJP.Camera", "No camera on this device");
      	      //Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG).show();
      	    } else {
      	    	try {
      	    		 cameraId = findFrontFacingCamera();
      	    	      if (cameraId < 0) {
      	    	    	  mCamera = Camera.open();//Toast.makeText(this, "No front facing camera found.",Toast.LENGTH_LONG).show();
      	    	      } else {
      	    	    	  mCamera = Camera.open(cameraId);
      	    	    	  Log.i("LNJP.Camera", "Camera Opened");
      	    	      }
      	    	      
      	    	      SurfaceView sv = new SurfaceView(getApplicationContext());
      	    	      
      	    		  try {
      	    			
      	    			  
      	    			  
      	   	                  mCamera.setPreviewDisplay(sv.getHolder());
      	   	                  parameters = mCamera.getParameters();
      	   	                  //Size bestPreviewSize = determineBestPreviewSize(parameters);
      	   	                  //Size bestPictureSize = determineBestPictureSize(parameters);
      	   	                  parameters.setPictureSize(640, 480);
      	   	             if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
      	   	                 parameters.setRotation(270);
      	   	                   //set camera parameters
      	   	        Log.i("LNJP.Camera", "Background image clicked and saved");
      	   	                 mCamera.setParameters(parameters);
      	   	            Log.i("LNJP.Camera", "Background image clicked and saved");
      	   	       
      	   	  

      	   	                 mCamera.startPreview();
      	   	            Thread thread = new Thread() {
      	   	           @Override
      	   	           public void run() {
      	   	               try {
      	   	                   
      	   	                       sleep(1000);
      	   	                      
      	   	                  mCamera.takePicture(null, null, mCall);
      	   	               } catch (InterruptedException e) {
      	   	                   e.printStackTrace();
      	   	               }
      	   	           }
      	   	       };

      	   	       thread.start();
      	   	                 
      	   	            Log.i("LNJP.Camera", "Background image clicked and saved");
      	   	                 
      	   	                 
      	   	            
      	   	            } catch (IOException e) {
  	    	   	         	if(mCamera!=null){
  	    	   	             mCamera.release();
  	    	   	         	}
      	   	                  // TODO Auto-generated catch block
      	   	                  e.printStackTrace();
      	   	            }
      	   	       		
      	    	      
      	   	         //sHolder = sv.getHolder();
      	   	        //tells Android that this surface will have its data constantly replaced
      	   	         //sHolder.setType(SurfaceHolder.SURFACE_TYPE_HARDWARE);
      	        }
      	        catch (Exception e){
      	        	if(mCamera!=null){
      	                mCamera.release();
      	            }
      	        	e.printStackTrace();
      	        	 Log.i("LNJP.Camera", "Camera already in use, background photo cant be clicked");
      	            // Camera is not available (in use or does not exist)
      	        }
      	     
      	    }

      	
      
      
      }
      private int findFrontFacingCamera() {
          int cameraId = -1;
          // Search for the front facing camera
          int numberOfCameras = Camera.getNumberOfCameras();
          for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
              Log.d("LNJP.Camera", "Camera found");
              cameraId = i;
              break;
            }
          }
          return cameraId;
        }
      private File getDir() {
  	    File sdDir =  Environment.getExternalStorageDirectory();
  	    return new File(sdDir, "Appolo/Background");
  	  }
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 
	}
	
	private class UploadWebPageTask extends AsyncTask<String, Void, String> {
		
		String file, url;
	    public UploadWebPageTask(String url, String filename) {
			// TODO Auto-generated constructor stub
	    	
	    	this.file=filename ;
	    	this.url=url;
		}

		@Override
	    protected String doInBackground(String... urls) {
	      String response = "";
	    
	    
	    String  upLoadServerUri ="http://"+AppVariable.getIP()+":8080/dqmsapi/ImageDownload?u="+AppVariable.getUsername()+"&p="+AppVariable.getPassword();
	      
	    
	    	 int res=uploadFile(url,upLoadServerUri,file);
	    Log.i("LNJP.Camera",String.valueOf(res));
	      
	      
	      
	      
		return response;
	     
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    	super.onPostExecute(result);
	    stopService(new Intent(CameraService.this, CameraService.class));
	    Log.i("LNJP.Camera", "Service Stoped");
	    	
	    }
	    
	    protected void onPreExecute() {
	    	
	    }
	  }
	
	
	public int uploadFile(String sourceFileUri, String upLoadServerUri ,String fname) {
           
		Log.i("LNJP.Camera","photoupload.Started");
        
        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = new File(sourceFileUri); 
         
        if (!sourceFile.isFile()) {
              
            
              
             Log.e("LNJP.Camera", " Upload Source File not exist :"
                                 +sourceFileUri);
              
            
              
             return 0;
          
        }
        else
        {
             try { 
                  
                   // open a URL connection to the Servlet
                 FileInputStream fileInputStream = new FileInputStream(sourceFile);
                 URL url = new URL(upLoadServerUri);
                  
                 // Open a HTTP  connection to  the URL
                 conn = (HttpURLConnection) url.openConnection(); 
                 conn.setDoInput(true); // Allow Inputs
                 conn.setDoOutput(true); // Allow Outputs
                 conn.setUseCaches(false); // Don't use a Cached Copy
                 conn.setRequestMethod("POST");
                 conn.setRequestProperty("Connection", "Keep-Alive");
                 conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                 conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                 conn.setRequestProperty("uploaded_file", fname); 
                  
                 dos = new DataOutputStream(conn.getOutputStream());
        
                 dos.writeBytes(twoHyphens + boundary + lineEnd); 
                 dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                         + fname+ "\"" + lineEnd);
                  
                 dos.writeBytes(lineEnd);
        
                 // create a buffer of  maximum size
                 bytesAvailable = fileInputStream.available(); 
        
                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
                 buffer = new byte[bufferSize];
        
                 // read file and write it into form...
                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                    
                 while (bytesRead > 0) {
                      
                   dos.write(buffer, 0, bufferSize);
                   bytesAvailable = fileInputStream.available();
                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                    
                  }
        
                 // send multipart form data necesssary after file data...
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        
                 // Responses from the server (code and message)
                 serverResponseCode = conn.getResponseCode();
                 String serverResponseMessage = conn.getResponseMessage();
                   
                 Log.i("LNJP.Camera", "PhotoUpload HTTP Response is : "
                         + serverResponseMessage + ": " + serverResponseCode);
                  
                
                  
                 //close the streams //
                 fileInputStream.close();
                 dos.flush();
                 dos.close();
           
            } catch (Exception e) {
                 
               
                e.printStackTrace();
                
                Log.e("LNJP.Camera", "Exception in PhotoUpload: "
                                                 + e.getMessage(), e);  
            }
                   
            return serverResponseCode; 
             
         } // End else block 
       } 

 
}