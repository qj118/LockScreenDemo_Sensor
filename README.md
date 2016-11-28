Lock Screen Demo
-----------------------------

## User Manual

1. Start APP
2. Tap "Enable"
3. When you cover the place of speaker, the phone will be locked.

## Main Technology

- Sensor
 + SensorManager -- get *SENSOR_SERVICE*
 + Sensor -- set sensor type
 + SensorEventListener -- response to sensor changes
registerListener -- start to response
unregisterListener -- stop reponsing
- Device
 + DevicePolicyManager -- get *DEVICE_POLICY_SERVICE*
 + DeviceAdminReceiver -- get a *DeviceAdminReceiver* object to start a intent to get device administration
- Notification
 + NotificationManager -- get *NOTIFICATION_SERVICE*
 + Notification.Builder -- set notification contents
 + PendingIntent -- when you click the notification, the MainActivity will be opened
 + Notification -- *builder.build()* and feed it to *notify* to start a notification
- This project is created in the Android Studio
- Path: app/src/main
