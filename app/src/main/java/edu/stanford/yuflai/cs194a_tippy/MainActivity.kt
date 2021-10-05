package edu.stanford.yuflai.cs194a_tippy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import kotlin.math.ceil
import kotlin.math.floor

private const val TAG = "MainActivity"
private const val INIT_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercent: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTipTotal: TextView

    // Self Extension
    private lateinit var cbRoundUp: CheckBox
    private lateinit var cbRoundDown: CheckBox
    private var RoundUp_checked: Boolean = false
    private var RoundDown_checked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercent = findViewById(R.id.tvTipPercent)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTipTotal = findViewById(R.id.tvTipTotal)
        // Self Extension
        cbRoundUp = findViewById(R.id.cbRoundUp)
        cbRoundDown = findViewById(R.id.cbRoundDown)

        // init value
        seekBarTip.progress = INIT_TIP_PERCENT
        tvTipPercent.text = "$INIT_TIP_PERCENT%"
        //cbRoundUp.isChecked = false
        //cbRoundDown.isChecked = false

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromuser: Boolean) {
                Log.i(TAG, "OnprogressChanged $progress")
                tvTipPercent.text = "$progress%"
                computeTipAndTotal()

            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        etBaseAmount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "AfterTextChanged $p0")
                computeTipAndTotal()
            }
        })
    }

    private fun computeTipAndTotal() {
        if(etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTipTotal.text = ""
        }else{
            // calculate rounded tips and display
            // 1. get value of tip and percent
            val baseAmount = etBaseAmount.text.toString().toDouble()
            val tipPercent = seekBarTip.progress
            // 2. compute the tip and total
            val tipAmount = baseAmount * tipPercent / 100
            val totalAmount = tipAmount + baseAmount
            var newTipAmount: Double
            var newTotalAmount: Double
            if (RoundUp_checked && !RoundDown_checked) {
                newTotalAmount = ceil(totalAmount)
                newTipAmount = tipAmount + (newTotalAmount - totalAmount)
            } else if (!RoundUp_checked && RoundDown_checked) {
                newTotalAmount = floor(totalAmount)
                newTipAmount = tipAmount + (newTotalAmount - totalAmount)
            } else {
                newTotalAmount = totalAmount
                newTipAmount = tipAmount
            }
            // 3. update the UI
            tvTipAmount.text = "%.2f".format(newTipAmount)
            tvTipTotal.text = "%.2f".format(newTotalAmount)
        }
    }
    // Self Extension: CheckBox onclick Handler
    public fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            when (view.id) {
                R.id.cbRoundUp -> {
                    // uncheck RoundDown
                    cbRoundDown.isChecked = false
                    // get value
                    RoundUp_checked = cbRoundUp.isChecked
                    RoundDown_checked = cbRoundDown.isChecked
                    // compute tip and display
                    computeTipAndTotal()
                }
                R.id.cbRoundDown -> {
                    // uncheck RoundUp
                    cbRoundUp.isChecked = false
                    // get value
                    RoundUp_checked = cbRoundUp.isChecked
                    RoundDown_checked = cbRoundDown.isChecked
                    // compute tip and display
                    computeTipAndTotal()
                }
            }
        }
    }
}
