import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.thinklab.Adapters.CustomAdapterCalendario;
import com.thinklab.Adapters.RowItemCalendario;
import com.thinklab.ecomodel.R;
import com.thinklab.ecomodel.login.Globalsinicio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class Calendar extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        JSONArray as = new JSONArray();
            as.put("HBD @eliasRuizHz");
            as.put("2015-05-16T09:00:00Z");
            as.put("2015-05-16T09:00:00Z");
        
        new Saveevent().execute(as);
    }

    private Date strtodate(String dtStart){
        Date date = null;
        try {
            SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            date = format.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

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

                    String p12Password = "notasecret";

                    KeyStore keystore = KeyStore.getInstance("PKCS12");
                    InputStream keyFileStream = getResources().openRawResource(getResources().getIdentifier("raw/key", "raw", getPackageName()));

                    if (keyFileStream == null){
                        throw new GeneralSecurityException("Key File Not Found.");
                    }

                    keystore.load(keyFileStream, p12Password.toCharArray());
                    PrivateKey key = (PrivateKey)keystore.getKey("privatekey", p12Password.toCharArray());

                    GoogleCredential credentials = new GoogleCredential.Builder().setTransport(AndroidHttp.newCompatibleTransport())
                            .setJsonFactory(new GsonFactory())
                            .setServiceAccountId("<Bla...Bla...Bla...Bla...Bla...>@developer.gserviceaccount.com")
                            .setServiceAccountScopes(Arrays.asList("https://www.googleapis.com/auth/calendar.readonly","https://www.googleapis.com/auth/calendar"))
                            .setServiceAccountPrivateKey(key)
                            .build();

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

                    Calendar.Events createdEvent = service.events();
                    Calendar.Events.Insert myevent = createdEvent.insert("primary", event);
                    Event excuter = myevent.execute();

                    System.out.println(excuter.getId());

                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
