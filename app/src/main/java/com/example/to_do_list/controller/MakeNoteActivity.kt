package com.example.to_do_list.controller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.to_do_list.R
import com.example.to_do_list.databinding.ActivityMakenoteBinding
import com.example.to_do_list.model.viewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MakeNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMakenoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMakenoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewPagerSetup()
        AddEvenBtnFloat()
    }

    private fun AddEvenBtnFloat() {
        var flag = false
        binding.apply {
            binding.fabBassFloat.setOnClickListener {
                flag = !flag
                if (flag) {
                    expand()
                } else {
                    close()
                }
            }
        }
        AddEventSubFloat1()
        AddEventSubFloat2()
    }

    private fun AddEventSubFloat1() {
        binding.fabSubFloat1.setOnClickListener {
            val i = Intent(this, InsertNoteActivity::class.java)
            startActivity(i)
        }
    }

    private fun AddEventSubFloat2() {
        binding.fabSubFloat2.setOnClickListener {
            val i = Intent(this, InsertScheduleActivity::class.java)
            startActivity(i)
        }
    }

    private fun close() {
        /*
             binding.fabBassFloat.animate().setDuration(300).rotation(0f).start()
            fabBassFloat.animate(): Bắt đầu một chuỗi hoạt ảnh trên nút fabBassFloat.
            .setDuration(300): Thiết lập thời gian chạy hoạt ảnh là 300ms (0,3 giây).
            .rotation(0f): Quay nút fabBassFloat về góc 0 độ. Đây có thể là một thao tác "reset" nếu trước đó nút đã xoay.
            .start(): Bắt đầu thực hiện chuỗi hoạt ảnh này.
         */

        binding.fabBassFloat.animate().setDuration(300).rotation(0f).start()


        /*
            binding.fabSubFloat1.animate().setDuration(300).translationY(0f).alpha(0f)
            fabSubFloat1.animate(): Bắt đầu một chuỗi hoạt ảnh trên nút fabSubFloat1.
            .setDuration(300): Thiết lập thời gian chạy hoạt ảnh là 300ms (0,3 giây).
            .translationY(0f): Di chuyển nút fabSubFloat1 theo trục Y về vị trí ban đầu (tọa độ Y = 0).
            .alpha(0f): Làm mờ dần nút fabSubFloat1, đặt độ mờ (alpha) thành 0, khiến nó trở nên trong suốt.

            .setInterpolator(AnticipateOvershootInterpolator(2f))
            AnticipateOvershootInterpolator(2f): Sử dụng một interpolator đặc biệt cho hoạt ảnh,
            tạo cảm giác nút "bật lại" một chút khi kết thúc. 2f là độ mạnh của hiệu ứng "anticipate" và "overshoot".
            Interpolator này làm cho hoạt ảnh trông tự nhiên hơn, giống như khi bạn đóng hoặc mở một cửa có chút lực đàn hồi.

            .withEndAction { binding.fabSubFloat1.visibility = View.GONE }
            .withEndAction { ... }: Xác định một hành động sẽ được thực hiện sau khi hoạt ảnh kết thúc.
            binding.fabSubFloat1.visibility = View.GONE: Sau khi hoạt ảnh kết thúc,
            đặt thuộc tính visibility của fabSubFloat1 thành GONE, khiến nó không còn hiển thị và chiếm không gian trên giao diện.
         */

        binding.fabSubFloat1.animate().setDuration(300).translationY(0f).alpha(0f)
            .setInterpolator(AnticipateOvershootInterpolator(2f)).withEndAction {
                binding.fabSubFloat1.visibility = View.GONE
            }.start()

        binding.fabSubFloat2.animate().setDuration(300).translationY(0f).alpha(0f)
            .setInterpolator(AnticipateOvershootInterpolator(2f)).withEndAction {
                binding.fabSubFloat2.visibility = View.GONE
            }.start()
    }

    private fun expand() {
        binding.fabBassFloat.animate().setDuration(300).rotation(180f).start()

        /*
            binding.fabSubFloat1.apply { ... }
            Phần này áp dụng hoạt ảnh cho nút phụ fabSubFloat1.

            alpha = 0f: Đặt độ mờ (alpha) ban đầu là 0, khiến nút trở nên trong suốt.
            visibility = View.VISIBLE: Hiển thị nút fabSubFloat1,
            cần thiết để cho phép hoạt ảnh hiển thị (vì nó có thể đã bị GONE từ trước).
            animate().setDuration(300): Bắt đầu chuỗi hoạt ảnh với thời gian chạy là 300ms.

            .translationY(-resources.getDimension(R.dimen.fab1)):
            Di chuyển fabSubFloat1 lên trên một khoảng cách bằng với giá trị trong R.dimen.fab1.
            Giá trị âm (-) làm cho nút di chuyển theo hướng lên trên.

            .alpha(1f): Thay đổi độ mờ (alpha) lên 1, làm cho nút dần dần hiện ra.
            .setInterpolator(OvershootInterpolator(2f)): Sử dụng OvershootInterpolator với hệ số 2f, tạo hiệu ứng bật nhẹ khi nút xuất hiện.
            .start(): Bắt đầu chuỗi hoạt ảnh.
         */

        binding.fabSubFloat1.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate().setDuration(300)
                .translationY(-resources.getDimension(R.dimen.fab1))
                .alpha(1f)
                .setInterpolator(OvershootInterpolator(2f))
                .start()
        }

        binding.fabSubFloat2.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate().setDuration(300)
                .translationY(-resources.getDimension(R.dimen.fab2))
                .alpha(1f)
                .setInterpolator(OvershootInterpolator(2f))
                .start()
        }
    }

    private fun ViewPagerSetup() {
        val adt = viewPagerAdapter(supportFragmentManager, lifecycle)
        binding.pgForTablayout.adapter = adt

        TabLayoutMediator(binding.tblOptions, binding.pgForTablayout) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.schedule)
                }

                1 -> {
                    tab.text = getString(R.string.note)
                }
            }
        }.attach()
    }
}