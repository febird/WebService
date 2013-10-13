/*Serve from Assets Files*/

@Override
            public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
                       Log.d(TAG,"SERVE ::  URI "+uri);
              final StringBuilder buf = new StringBuilder();
              for (Entry<Object, Object> kv : header.entrySet())
                buf.append(kv.getKey() + " : " + kv.getValue() + "\n");
              InputStream mbuffer = null;



                            try { 
                                    if(uri!=null){

                                       if(uri.contains(".js")){
                                               mbuffer = mContext.getAssets().open(uri.substring(1));
                                               return new NanoHTTPD.Response(HTTP_OK, MIME_JS, mbuffer);
                                       }else if(uri.contains(".css")){
                                               mbuffer = mContext.getAssets().open(uri.substring(1));
                                               return new NanoHTTPD.Response(HTTP_OK, MIME_CSS, mbuffer);

                                       }else if(uri.contains(".png")){
                                               mbuffer = mContext.getAssets().open(uri.substring(1));      
                                               // HTTP_OK = "200 OK" or HTTP_OK = Status.OK;(check comments)
                                               return new NanoHTTPD.Response(HTTP_OK, MIME_PNG, mbuffer);
                                       }else if (uri.contains("/mnt/sdcard")){
                                               Log.d(TAG,"request for media on sdCard "+uri);
                                               File request = new File(uri);
                                               mbuffer = new FileInputStream(request);
                                               FileNameMap fileNameMap = URLConnection.getFileNameMap();
                                               String mimeType = fileNameMap.getContentTypeFor(uri);

                                               Response streamResponse = new Response(HTTP_OK, mimeType, mbuffer);
                                               Random rnd = new Random();
                                String etag = Integer.toHexString( rnd.nextInt() );
                                streamResponse.addHeader( "ETag", etag);
                                               streamResponse.addHeader( "Connection", "Keep-alive");






                                               return streamResponse;
                                       }else{
                                               mbuffer = mContext.getAssets().open("index.html");
                                               return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, mbuffer);
                                       }
                                    }

                            } catch (IOException e) {
                                    Log.d(TAG,"Error opening file"+uri.substring(1));
                                    e.printStackTrace();
                            }

                      return null;

            }