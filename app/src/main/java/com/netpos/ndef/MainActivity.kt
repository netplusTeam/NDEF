package com.netpos.ndef

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast


class MainActivity : Activity() {
    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize NFC adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            // NFC is not available on this device
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        if (!nfcAdapter!!.isEnabled) {
            // NFC is disabled, prompt user to enable NFC in settings
            Toast.makeText(this, "Please enable NFC", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Create a PendingIntent for NFC foreground dispatch
        val intent = Intent(this, javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onResume() {
        super.onResume()
        // Enable NFC foreground dispatch
//        val intent = Intent(this, javaClass)
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//        nfcAdapter!!.enableForegroundDispatch(this, intent, null, null)

        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        // Disable NFC foreground dispatch
        nfcAdapter!!.disableForegroundDispatch(this)
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Handle NFC tag discovery in onNewIntent method
        Log.d("FRIST_CHECK", "FRIST_CHECK")
        if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            // Extract NDEF message from intent
            val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (rawMessages != null) {
                // Process each NDEF message
                for (message in rawMessages) {
                    // Convert to NdefMessage
                    val ndefMessage = message as? NdefMessage
                    if (ndefMessage != null) {
                        // Process NDEF records
                        for (record in ndefMessage.records) {
                            // Extract and process record payload
                            val payload = String(record.payload)
                            // Handle payload data as needed
                            findViewById<TextView>(R.id.new_text).text = payload
                        }
                    }
                }
            }
        }
    }

//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        // Handle NFC tag discovery
//        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
//        Log.d("FRIST_CHECK", "FRIST_CHECK")
//        if (tag != null) {
//            val ndef = Ndef.get(tag)
//            Log.d("THIR_CHECK", "THIR_CHECK")
//            if (ndef != null) {
//                Log.d("SEC_CHECK", "SEC_CHECK")
//                try {
//                    Log.d("FOUR_CHECK", "FOUR_CHECK")
//                    ndef.connect()
//                    val ndefMessage = ndef.ndefMessage
//                    if (ndefMessage != null) {
//                        Log.d("FRI_CHECK", "FRI_CHECK")
//                        val records = ndefMessage.records
//                        for (record in records) {
//                            Log.d("SAT_CHECK", "SAT_CHECK")
//                            // Process NDEF record, e.g., extract payload
//                            val textPayload = String(record.payload)
//                            findViewById<TextView>(R.id.new_text).text = textPayload
//                            Toast.makeText(
//                                this,
//                                "NFC Tag Content: $textPayload",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                    ndef.close()
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            } else {
//                findViewById<TextView>(R.id.new_text).text = "NDEF is not supported by this NFC tag"
//                Toast.makeText(this, "NDEF is not supported by this NFC tag", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }
}
