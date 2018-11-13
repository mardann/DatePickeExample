package il.co.procyon.datepicker

import android.app.DatePickerDialog
import android.databinding.ObservableBoolean
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import il.co.procyon.datepicker.databinding.ExpandableLoansBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.expandable_loans.view.*
import java.util.*

class MainActivity : AppCompatActivity() {


    //Date stuff:
    val initDate = DateObject(2018, 5, 15)
    var selectedDate = initDate.copy()

    //Expandable list stuff:
    val loansInflatedFlag = ObservableBoolean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupDatePicker()

        setupExpandableLoans(testExpandableData)
    }


    //date picker logic

    private fun setupDatePicker() {
        button.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, datePickedListener, selectedDate.year, selectedDate.month, selectedDate.dayOfMonth)
            val cal = Calendar.getInstance()
            cal.set(initDate.year, initDate.month, initDate.dayOfMonth)
            datePickerDialog.datePicker.minDate = cal.timeInMillis

            datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "אישור", datePickerDialog)
            datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "ביטול", datePickerDialog)

            datePickerDialog.setCancelable(false)


            datePickerDialog.show()
        }
    }

    private val datePickedListener: (DatePicker, Int, Int, Int) -> Unit = { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->

        Toast.makeText(this, "date: $dayOfMonth/${month + 1}/$year", Toast.LENGTH_LONG).show()

        selectedDate.also {
            it.year = year
            it.month = month
            it.dayOfMonth = dayOfMonth
        }

    }

    //expandable list logic (notice - this uses DataBinding, so copy relevant layout as well)
    private fun setupExpandableLoans(data: ExpandableData) {
        val binder = ExpandableLoansBinding.bind(loans_layout)
        binder.isExpanded = loansInflatedFlag

        data.items.forEach {
            val item: TextView = layoutInflater.inflate(R.layout.loan_item, null, false) as TextView
            item.text = it
            binder.root.items_container.addView(item)
        }

        binder.root.my_loans_title.text = data.header
        binder.root.my_loans_title.setOnClickListener { loansInflatedFlag.set(!loansInflatedFlag.get()) }
    }

    private val testExpandableData: ExpandableData = ExpandableData(
            "לרשותך 2 כרטיסים והלוואה 1",
            listOf(
                    "לאומי קארד מאסטרקארד (1234)",
                    "לאומי קארד ויזה (9876)",
                    "הלוואה ללא כרטיס"
            )
    )


}
