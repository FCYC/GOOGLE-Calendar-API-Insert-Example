## Calendar API Insert Example (Google)

It is an easy and practical example of using the Google Calendar API, just insert mode.

* The call is made to AsyncTask and is sent a JSONArray.
```Java
@Override
    protected void onCreate(Bundle savedInstanceState) {
        JSONArray as = new JSONArray();
            as.put("HBD @eliasRuizHz");
            as.put("2015-05-16T09:00:00Z");
            as.put("2015-05-16T09:00:00Z");
        
        new Saveevent().execute(as);
    }
```

* The AsyncTask receives data and assigns.
```Java
private class Saveevent extends AsyncTask<JSONArray, Void, String>{


        @Override
        protected String doInBackground(JSONArray... params) {
            try{
                try {
                    JSONArray aqui = params[0];
                    String concepto = null;
                    String startDates = null;
                    String finalDates = null;
                    try {
                        concepto = aqui.getString(0);
                        startDates = aqui.getString(1);
                        finalDates = aqui.getString(2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
```

* Add the 'res/raw/key.p12' that release of Google Console and read.
```Java
String p12Password = "notasecret";
KeyStore keystore = KeyStore.getInstance("PKCS12");
InputStream keyFileStream = getResources().openRawResource(getResources().getIdentifier("raw/key", "raw", getPackageName()));
if (keyFileStream == null){
    throw new GeneralSecurityException("Key File Not Found.");
}

keystore.load(keyFileStream, p12Password.toCharArray());
PrivateKey key = (PrivateKey)keystore.getKey("privatekey", p12Password.toCharArray());
```

* Add your key to your credentials as his "ID" in this format (<Bla...Bla...Bla...Bla...Bla...>@developer.gserviceaccount.com).
```Java
GoogleCredential credentials = new GoogleCredential.Builder().setTransport(AndroidHttp.newCompatibleTransport())
                            .setJsonFactory(new GsonFactory())
                            .setServiceAccountId("<Bla...Bla...Bla...Bla...Bla...>@developer.gserviceaccount.com")
                            .setServiceAccountScopes(Arrays.asList("https://www.googleapis.com/auth/calendar.readonly","https://www.googleapis.com/auth/calendar"))
                            .setServiceAccountPrivateKey(key)
                            .build();
```

* Initialize the service and creation of event.
```Java
Calendar service = new Calendar.Builder(AndroidHttp.newCompatibleTransport(),  new GsonFactory(), credentials).setApplicationName("HappyDayElias").build();

                    Event event = new Event();
                    event.setSummary(concepto);
                    event.setLocation("Eart");
                    event.setColorId("10");

                    ArrayList<EventAttendee> attendees = new ArrayList<EventAttendee>();

                    String gmail = null;
                    Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
                    Account[] accounts = AccountManager.get(Calendario.this).getAccounts();
                    for (Account account : accounts) {
                        if (gmailPattern.matcher(account.name).matches()) {
                            gmail = account.name;
                        }
                    }

                    attendees.add(new EventAttendee().setEmail(gmail));
                    event.setAttendees(attendees);

                    Date startDate = strtodate(startDates);
                    Date endDate = strtodate(finalDates);
                    DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
                    event.setStart(new EventDateTime().setDateTime(start));
                    DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
                    event.setEnd(new EventDateTime().setDateTime(end));
```

* There is only run for insertion into the user's calendar.
```Java
Calendar.Events createdEvent = service.events();
Calendar.Events.Insert myevent = createdEvent.insert("primary", event);
Event excuter = myevent.execute();
```

License
----

MIT
