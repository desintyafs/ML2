package com.mobile.desinuas

import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class SimActivity : AppCompatActivity() {

    private lateinit var interpreter: Interpreter
    private val mModelPath = "pred.tflite"

    private lateinit var resultText: TextView
    private lateinit var battery_power: EditText
    private lateinit var blue: EditText
    private lateinit var clock_speed: EditText
    private lateinit var dual_sim: EditText
    private lateinit var fc: EditText
    private lateinit var four_g: EditText
    private lateinit var int_memory: EditText
    private lateinit var m_dep: EditText
    private lateinit var mobile_wt: EditText
    private lateinit var n_cores: EditText
    private lateinit var pc: EditText
    private lateinit var px_height: EditText
    private lateinit var px_width: EditText
    private lateinit var ram: EditText
    private lateinit var sc_h: EditText
    private lateinit var sc_w: EditText
    private lateinit var talk_time: EditText
    private lateinit var three_g: EditText
    private lateinit var touch_screen: EditText
    private lateinit var wifi: EditText
    private lateinit var Predict: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sim)

        resultText = findViewById(R.id.txtResult)
        battery_power = findViewById(R.id.battery_power)
        blue = findViewById(R.id.blue)
        clock_speed = findViewById(R.id.clock_speed)
        dual_sim = findViewById(R.id.dual_sim)
        fc = findViewById(R.id.fc)
        four_g = findViewById(R.id.four_g)
        int_memory = findViewById(R.id.int_memory)
        m_dep = findViewById(R.id.m_dep)
        mobile_wt = findViewById(R.id.mobile_wt)
        n_cores = findViewById(R.id.n_cores)
        pc = findViewById(R.id.pc)
        px_height = findViewById(R.id.px_height)
        px_width = findViewById(R.id.px_width)
        ram = findViewById(R.id.ram)
        sc_h = findViewById(R.id.sc_h)
        sc_w = findViewById(R.id.sc_w)
        talk_time = findViewById(R.id.talk_time)
        three_g = findViewById(R.id.three_g)
        touch_screen = findViewById(R.id.touch_screen)
        wifi = findViewById(R.id.wifi)
        Predict = findViewById(R.id.btnCheck)

        Predict.setOnClickListener {
            Log.d("SimulationActivity", "Predict button clicked")

            // Validate inputs
            if (!validateInputs()) {
                resultText.text = "Harap Isi dahulu semua Pertanyaan dengan benar"
                return@setOnClickListener
            }

            var result = doInference(
                battery_power.text.toString(),
                blue.text.toString(),
                clock_speed.text.toString(),
                dual_sim.text.toString(),
                fc.text.toString(),
                four_g.text.toString(),
                int_memory.text.toString(),
                m_dep.text.toString(),
                mobile_wt.text.toString(),
                n_cores.text.toString(),
                pc.text.toString(),
                px_height.text.toString(),
                px_width.text.toString(),
                ram.text.toString(),
                sc_h.text.toString(),
                sc_w.text.toString(),
                talk_time.text.toString(),
                three_g.text.toString(),
                touch_screen.text.toString(),
                wifi.text.toString()
            )

            Log.d("SimulationActivity", "Inference result: $result")

            runOnUiThread {
                when (result) {
                    0 -> resultText.text = "Spec Low Price"
                    1 -> resultText.text = "Spec Medium Price"
                    2 -> resultText.text = "Spec High Price"
                    3 -> resultText.text = "Spec Flagship"
                    else -> resultText.text = "Unknown"
                }
            }
        }
        initInterpreter()
    }

    private fun validateInputs(): Boolean {
        // Check if all inputs are filled and valid
        val inputs = listOf(
            battery_power, blue, clock_speed, dual_sim, fc,
            four_g, int_memory, m_dep,
            mobile_wt, n_cores,
            pc, px_height, px_width, ram,
            sc_h, sc_w, talk_time, three_g,
            touch_screen, wifi
        )

        return inputs.all { it.text.toString().isNotEmpty() }
    }

    private fun initInterpreter() {
        val options = Interpreter.Options().also {
            it.setNumThreads(4)  // Using 4 threads instead of 25
            it.setUseNNAPI(true)
            interpreter = Interpreter(loadModelFile(assets, mModelPath), it)
        }
    }

    private fun doInference(
        input1: String,
        input2: String,
        input3: String,
        input4: String,
        input5: String,
        input6: String,
        input7: String,
        input8: String,
        input9: String,
        input10: String,
        input11: String,
        input12: String,
        input13: String,
        input14: String,
        input15: String,
        input16: String,
        input17: String,
        input18: String,
        input19: String,
        input20: String,
    ): Int {
        val inputVal = FloatArray(20)

        inputVal[0] = input1.toFloat()
        inputVal[1] = input2.toFloat()
        inputVal[2] = input3.toFloat()
        inputVal[3] = input4.toFloat()
        inputVal[4] = input5.toFloat()
        inputVal[5] = input6.toFloat()
        inputVal[6] = input7.toFloat()
        inputVal[7] = input8.toFloat()
        inputVal[8] = input9.toFloat()
        inputVal[9] = input10.toFloat()
        inputVal[10] = input11.toFloat()
        inputVal[11] = input12.toFloat()
        inputVal[12] = input13.toFloat()
        inputVal[13] = input14.toFloat()
        inputVal[14] = input15.toFloat()
        inputVal[15] = input16.toFloat()
        inputVal[16] = input17.toFloat()
        inputVal[17] = input18.toFloat()
        inputVal[18] = input19.toFloat()
        inputVal[19] = input20.toFloat()

        val output = Array(1) { FloatArray(4) }
        interpreter.run(inputVal, output)
        return output[0][0].toInt()
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

}