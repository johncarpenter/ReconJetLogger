# ReconJetLogger

This application is a simple android app designed to run on the Recon Jet device and log location and sensor information to a gpx file.

### Installing 

*The application will have to be side-loaded onto a Recon Jet with the USB Debugging enabled. Leave a comment in the issues if you need help with this.*

The application runs as a service listening for activity starts from the Recon system. So to trigger the loading..

1. Start the app, from My Apps, Activity Logger. It should have a screen saying the activity has started in green. 
2. Launch a tracking activity as usual on the device. Once the tracking starts/stops a notification will appear on the bottom indicating that the logging is in process. 
3. Logs can be found by mounting the device and browsing the sdcard under GPXLogger/


### Demonstration and License. 

This program is for demonstration purposes only, no guarantees are made. I didn't even write tests for it so beware. 

### License

```
Copyright 2017 2Lines Software Inc

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
