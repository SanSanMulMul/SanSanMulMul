import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.databinding.FragmentGroupHikingStyleBinding
import com.sansantek.sansanmulmul.ui.util.Util.dp
import com.sansantek.sansanmulmul.ui.util.Util.dpToPx

class GroupHikingStyleFragment : Fragment() {

    private lateinit var binding: FragmentGroupHikingStyleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupHikingStyleBinding.inflate(inflater, container, false)

        // Set listeners for each CheckBox
        binding.cbHikingStyle1.setOnCheckedChangeListener(onCheckedChangeListener(R.color.purple))
        binding.cbHikingStyle2.setOnCheckedChangeListener(onCheckedChangeListener(R.color.lightgreen))
        binding.cbHikingStyle3.setOnCheckedChangeListener(onCheckedChangeListener(R.color.blue))
        binding.cbHikingStyle4.setOnCheckedChangeListener(onCheckedChangeListener(R.color.pink))
        binding.cbHikingStyle5.setOnCheckedChangeListener(onCheckedChangeListener(R.color.yellow))

        return binding.root
    }

    private fun onCheckedChangeListener(color: Int): CompoundButton.OnCheckedChangeListener {
        return object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
                val checkBox = compoundButton as CheckBox
                if (isChecked) {
                    setGradient(checkBox, color)
                } else {
                    // Reset to the original drawable background
                    checkBox.background = ContextCompat.getDrawable(requireContext(), R.drawable.check_box_background)
                }
            }
        }
    }

    private fun setGradient(checkBox: CheckBox, color: Int) {
        // Create a gradient drawable with stroke and corner radius
        val gradientDrawable = GradientDrawable().apply {
            orientation = GradientDrawable.Orientation.TOP_BOTTOM
            colors = intArrayOf(
                Color.WHITE, // Starting color
                ContextCompat.getColor(requireContext(), color) // Dynamic color from resources
            )
            val dp = 10f.dpToPx(this@GroupHikingStyleFragment.requireContext())
            cornerRadius = dp // Set the corner radius
            setStroke(1, Color.parseColor("#808080")) // Set stroke width and color
        }
        checkBox.background = gradientDrawable // Set the gradient as background
    }
}