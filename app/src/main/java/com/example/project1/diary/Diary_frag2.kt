package com.example.project1.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project1.R

class Diary_frag2 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Arguments에서 데이터를 가져와 사용하는 부분
        arguments?.let {
            val num = it.getInt("number")
            // 필요한 경우 'num'을 사용하여 추가적인 작업을 수행합니다.
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment1의 XML 레이아웃 파일을 인플레이트하여 반환합니다.
        return inflater.inflate(R.layout.fragment_diary_frag2, container, false) // XML 파일명 수정
    }

    companion object {
        fun newInstance(number: Int): Diary_frag2 {
            val fragment1 = Diary_frag2()
            val bundle = Bundle()
            bundle.putInt("number", number)
            fragment1.arguments = bundle
            return fragment1
        }
    }
}
