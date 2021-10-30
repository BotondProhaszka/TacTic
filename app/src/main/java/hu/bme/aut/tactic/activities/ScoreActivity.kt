package hu.bme.aut.tactic.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.tactic.databinding.ActivityScoreBinding

class ScoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}