package com.example.myapplication.Memo

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import org.threeten.bp.LocalDate

class CalenderActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName
    private lateinit var calendarView: MaterialCalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)
        calendarView = findViewById(R.id.calendarview)

        title="오늘의 날짜는?"

        // 월, 요일을 한글로 보이게 설정 (MonthArrayTitleFormatter의 작동을 확인하려면 밑의 setTitleFormatter()를 지운다)
        calendarView.setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
        calendarView.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))
        // 좌우 화살표 사이 연, 월의 폰트 스타일 설정
        calendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader)

        val request=intent.getStringExtra("request")
        val memo_intent= Intent(this,MemoActivity::class.java)


        //날짜 하나 선택했을 때 클릭리스너
        calendarView.setOnDateChangedListener { widget, date, selected ->
            var startday = date.date.toString()

            memo_intent.putExtra("date",startday)
            Log.d("memo date",startday)

            startActivity(memo_intent)
        }


        // 좌우 화살표 가운데의 연/월이 보이는 방식 커스텀
        calendarView.setTitleFormatter(object : TitleFormatter {
            override fun format(day: CalendarDay): CharSequence {
                // CalendarDay라는 클래스는 LocalDate 클래스를 기반으로 만들어진 클래스다
                // 때문에 MaterialCalendarView에서 연/월 보여주기를 커스텀하려면 CalendarDay 객체의 getDate()로 연/월을 구한 다음 LocalDate 객체에 넣어서
                // LocalDate로 변환하는 처리가 필요하다
                val inputText = day.date
                val calendarHeaderElements = inputText.toString().split("-").toTypedArray()
                val calendarHeaderBuilder = StringBuilder()
                calendarHeaderBuilder.append(calendarHeaderElements[0])
                    .append(" ")
                    .append(calendarHeaderElements[1])
                return calendarHeaderBuilder.toString()
            }
        })
    }

    /* 선택된 요일의 background를 설정하는 Decorator 클래스 */
    private class DayDecorator(context: Context) : DayViewDecorator {
        private val drawable: Drawable?

        init {
            drawable = ContextCompat.getDrawable(context, R.drawable.calendar_selector)
        }

        // true를 리턴 시 모든 요일에 내가 설정한 드로어블이 적용된다
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return true
        }

        // 일자 선택 시 내가 정의한 드로어블이 적용되도록 한다
        override fun decorate(view: DayViewFacade?) {
            view?.setSelectionDrawable(drawable!!)
        }
    }
}