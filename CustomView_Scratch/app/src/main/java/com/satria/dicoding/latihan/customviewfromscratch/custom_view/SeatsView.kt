package com.satria.dicoding.latihan.customviewfromscratch.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.satria.dicoding.latihan.customviewfromscratch.R
import com.satria.dicoding.latihan.customviewfromscratch.data.Seat

class SeatsView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val seats: ArrayList<Seat> = arrayListOf(
        Seat(id = 1, code = "A1", isBooked = false),
        Seat(id = 2, code = "A2", isBooked = false),
        Seat(id = 3, code = "b1", isBooked = false),
        Seat(id = 4, code = "B2", isBooked = false),
        Seat(id = 5, code = "C1", isBooked = false),
        Seat(id = 6, code = "C2", isBooked = false),
        Seat(id = 7, code = "D1", isBooked = false),
        Seat(id = 8, code = "D2", isBooked = false),
        Seat(id = 9, code = "E1", isBooked = false),
    )

    var seat: Seat? = null

    private val bgPaint = Paint()
    private val armRestPaint = Paint()
    private val bottomSeatPaint = Paint()
    private val mBounds = Rect()
    private val numberSeatPaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)
    private val titlePaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)

        val halfOfHeight = height / 2
        val halfOfWidth = width / 2
        var value = -600f

        for (i in seats.indices) {
            if (i.mod(2) == 0) {
                seats[i].apply {
                    x = halfOfWidth - 300f
                    y = halfOfHeight + value
                }
            } else {
                seats[i].apply {
                    x = halfOfWidth + 100f
                    y = halfOfHeight + value
                }
                value += 300f
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (seat in seats) {
            drawSeat(canvas, seat)
        }
        val text = "Silahkan Pilih Kursi"
        titlePaint.textSize = 50f
        canvas?.drawText(text, (width / 2f) - 197f, 100f, titlePaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == ACTION_DOWN) {
            val halfOfHeight = height / 2
            val halfOfWidth = width / 2

            // tentukan ukuran touch untuk tiap kolom
            val widthColumnOne = (halfOfWidth - 300f)..(halfOfWidth - 100f)
            val widthColumnTwo = (halfOfWidth + 100f)..(halfOfWidth + 300f)

            // tentukan ukuran touch height untuk tiap kolom
            val listOfHeightRow = ArrayList<ClosedFloatingPointRange<Float>>()
            var sizeX = -400F
            var sizeY = -600F
            for (i in seats.indices) {
                val heightRow = (halfOfHeight + sizeY)..(halfOfHeight + sizeX)
                listOfHeightRow.add(heightRow)
                sizeX += 300F
                sizeY += 300F
            }

            var pos = 0
            for (i in listOfHeightRow) {
                when {
                    event.x in widthColumnOne && event.y in i -> {
                        booking(pos)
                        break
                    }

                    event.x in widthColumnTwo && event.y in i -> {
                        pos += 1
                        booking(pos)
                        break
                    }
                }
                pos += 2
            }
        }

        return true
    }

    private fun booking(position: Int) {
        for (seat in seats) {
            seat.isBooked = false
        }
        seats[position].apply {
            seat = this
            isBooked = true
        }
        invalidate()
    }

    private fun drawSeat(canvas: Canvas?, seat: Seat) {
        // Atur warna ketika dipilih/sudah dibooking
        if (seat.isBooked) {
            bgPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            armRestPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            bottomSeatPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            numberSeatPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
        } else {
            bgPaint.color = ResourcesCompat.getColor(resources, R.color.blue_500, null)
            armRestPaint.color = ResourcesCompat.getColor(resources, R.color.blue_700, null)
            bottomSeatPaint.color = ResourcesCompat.getColor(resources, R.color.blue_200, null)
            numberSeatPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
        }

        // Simpan State
        canvas?.save()

        // Background
        canvas?.translate(seat.x as Float, seat.y as Float)
        val bgPath = Path()
        bgPath.addRect(0f, 0f, 200f, 200f, Path.Direction.CCW)
        bgPath.addCircle(100f, 50f, 75f, Path.Direction.CCW)
        canvas?.drawPath(bgPath, bgPaint)

        // Arm rest
        val armRestPath = Path()
        armRestPath.addRect(0f, 0f, 50f, 200f, Path.Direction.CCW)
        canvas?.drawPath(armRestPath, armRestPaint)
        canvas?.translate(150f, 0f)
        armRestPath.addRect(0f, 0f, 50f, 200f, Path.Direction.CCW)
        canvas?.drawPath(armRestPath, armRestPaint)

        // Bagian bawah kursi
        canvas?.translate(-150f, 175f)
        val bottomSeatPath = Path()
        bottomSeatPath.addRect(0f, 0f, 200f, 25f, Path.Direction.CCW)
        canvas?.drawPath(bottomSeatPath, bottomSeatPaint)

        // Nomor Kursi
        canvas?.translate(0f, -175f)
        numberSeatPaint.apply {
            textSize = 50F
            numberSeatPaint.getTextBounds(seat.code, 0, seat.code.length, mBounds)
        }
        canvas?.drawText(seat.code, 100f - mBounds.centerX(), 100f, numberSeatPaint)

        // restore
        canvas?.restore()
    }


}