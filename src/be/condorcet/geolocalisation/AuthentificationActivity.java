/*package be.condorcet.geolocalisation;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class AuthentificationActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentification);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authentification, menu);
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
}*/

package be.condorcet.geolocalisation;

import java.sql.Connection;
import java.util.ArrayList;

import myconnections.DBConnection;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import entities.CategorieDB;
import entities.UserDB;

public class AuthentificationActivity extends ActionBarActivity {

	private Button bti;
	private Button bta;
	private EditText ednom;
	private EditText edprenom;
	private EditText edlogin;
	private EditText edpasswd;
	private EditText edlogincnx;
	private EditText edpasswdcnx;
	private Connection con = null;
	public static final String IDUSER = "userdb";
	public static final String RESULTAT = "resultat";
	public static final String CATEGORIE = "categorie";
	private boolean trouve = false, unique = false;
	private String champvide, pasinscrit, existedeja, authentifier, ins, echec,
			patientez, inscrit, authentification;
	private ArrayList<String> cat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentification);

		ednom = (EditText) findViewById(R.id.ednom);
		edprenom = (EditText) findViewById(R.id.edprenom);
		edlogin = (EditText) findViewById(R.id.edlogin);
		edpasswd = (EditText) findViewById(R.id.edpasswd);
		edlogincnx = (EditText) findViewById(R.id.edloginconnexion);
		edpasswdcnx = (EditText) findViewById(R.id.edpasswdconnexion);
		champvide = getResources().getString(R.string.champvide);
		pasinscrit = getResources().getString(R.string.pasinscrit);
		inscrit = getResources().getString(R.string.inscrit);
		existedeja = getResources().getString(R.string.existedeja);
		authentifier = getResources().getString(R.string.authentifier);
		authentification = getResources().getString(R.string.authentification);
		ins = getResources().getString(R.string.ins);
		echec = getResources().getString(R.string.echec);
		patientez = getResources().getString(R.string.patientez);

		bti = (Button) findViewById(R.id.btinscrire);
		bti.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// edmsg.setText(ins);
				MyAccesDBIns adb = new MyAccesDBIns(
						AuthentificationActivity.this);
				adb.execute();
			}
		});

		bta = (Button) findViewById(R.id.btconnecter);
		bta.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// edmsg.setText(authentifier);
				MyAccesDBCnx adb = new MyAccesDBCnx(
						AuthentificationActivity.this);
				adb.execute();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authentification, menu);
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			con.close();
			con = null;
			Log.d("connexion", "deconnexion OK");
		} catch (Exception e) {
		}
		Log.d("connexion", "deconnexion OK");
	}

	class MyAccesDBIns extends AsyncTask<String, Integer, Boolean> {
		private String resultat;
		private ProgressDialog pgd = null;
		private String nom, prenom, login, passwd;

		public MyAccesDBIns(AuthentificationActivity pActivity) {
			link(pActivity);
			// TODO Auto-generated constructor stub
		}

		private void link(AuthentificationActivity pActivity) {
			// TODO Auto-generated method stub
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(AuthentificationActivity.this);
			pgd.setMessage(ins);
			pgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pgd.setTitle(patientez);
			pgd.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			if (con == null) {// premier invocation
				con = new DBConnection().getConnection();
				if (con == null) {
					resultat = echec;
					return false;
				}
				UserDB.setConnection(con);
			}
			// int id=Integer.parseInt(ed.getText().toString());
			nom = ednom.getText().toString();
			prenom = edprenom.getText().toString();
			login = edlogin.getText().toString();
			passwd = edpasswd.getText().toString();
			try {
				UserDB u = new UserDB(nom, prenom, login, passwd);
				u.create();
				u.read();
				unique = true;
				// resultat = u.toString();
			} catch (Exception e) {
				// resultat = e.getMessage();
				return false;
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pgd.dismiss();
			// edmsg.setText(resultat);
			if (resultat == echec) {
				Toast.makeText(AuthentificationActivity.this, echec,
						Toast.LENGTH_LONG).show();
				return;
			}
			if (nom.trim().equals("") || prenom.trim().equals("")
					|| login.trim().equals("") || passwd.trim().equals("")) {
				Toast.makeText(AuthentificationActivity.this, champvide,
						Toast.LENGTH_LONG).show();
				return;
			}
			if (!unique) {
				Toast.makeText(AuthentificationActivity.this, existedeja,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(AuthentificationActivity.this, inscrit,
						Toast.LENGTH_LONG).show();
			}

		}
	}

	class MyAccesDBCnx extends AsyncTask<String, Integer, Boolean> {
		private String resultat, logincnx, passwdcnx;
		private ProgressDialog pgd = null;
		private UserDB u = null;

		public MyAccesDBCnx(AuthentificationActivity pActivity) {
			link(pActivity);
			// TODO Auto-generated constructor stub
		}

		private void link(AuthentificationActivity pActivity) {
			// TODO Auto-generated method stub
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(AuthentificationActivity.this);
			pgd.setMessage(authentifier);
			pgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pgd.setTitle(patientez);
			pgd.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			if (con == null) {// premier invocation
				con = new DBConnection().getConnection();
				if (con == null) {
					resultat = echec;
					return false;
				}
				UserDB.setConnection(con);
				CategorieDB.setConnection(con);
			}
			// int id=Integer.parseInt(ed.getText().toString());
			logincnx = edlogincnx.getText().toString();
			passwdcnx = edpasswdcnx.getText().toString();
			try {
				UserDB u = new UserDB(logincnx, passwdcnx);
				u.connexion();
				cat = CategorieDB.liste();
				trouve = true;
				// resultat = u.toString();
			} catch (Exception e) {
				// resultat = e.getMessage();
				// resultat = pasinscrit;
				return false;
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pgd.dismiss();
			// edmsg.setText(resultat);
			Intent i = new Intent(AuthentificationActivity.this,
					MenuActivity.class);
			i.putExtra(IDUSER, u);
			i.putExtra(CATEGORIE, cat);
			// i.putExtra(RESULTAT, resultat);
			if (resultat == echec) {
				Toast.makeText(AuthentificationActivity.this, echec,
						Toast.LENGTH_LONG).show();
				return;
			}
			if (logincnx.trim().equals("") || passwdcnx.trim().equals("")) {
				Toast.makeText(AuthentificationActivity.this, champvide,
						Toast.LENGTH_LONG).show();
				return;
			}
			if (!trouve) {
				Toast.makeText(AuthentificationActivity.this, pasinscrit,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(AuthentificationActivity.this, authentification,
						Toast.LENGTH_LONG).show();
				trouve = false;
				startActivity(i);
			}
		}
	}
}
