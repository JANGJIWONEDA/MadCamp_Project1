# "여행 기록지"

여행 기록지는 여행을 기록할 수 있는 어플 입니다.

## Info

Team: KAIST 19' 김동근 , DGIST 22' 장지원

Duration: 2024.06.26 ~ 2024.07.03

At: KAIST Mad Camp

## Language / IDE

Language: Kotlin

IDE: Android Studio

## Introduction / Implementation

<p>저희 어플은 여행의 기록을 메모할 수 있는 어플입니다.</p>  

  
<div style="display: flex; flex-wrap: nowrap; justify-content: center;">
    <img src="https://github.com/JANGJIWONEDA/MadCamp_Project1/assets/133734191/a39c6dac-1566-4980-b733-1b0b92dc30c7" alt="splash_gif" width="180" style="margin-right: 30px;">
    <img src="https://github.com/JANGJIWONEDA/MadCamp_Project1/assets/133734191/2f59af74-59ae-4ce0-9313-2db8c6099b85" alt="Menu" width="180" style="margin-right: 30px;">
    <img src="https://github.com/JANGJIWONEDA/MadCamp_Project1/assets/133734191/44201ea5-81a7-4cdf-894e-fe3c519c2a47" alt="Screen_Recording_20240703_140043-ezgif com-video-to-gif-converter" width="180" style="margin-right: 30px;">
    <img src="https://github.com/JANGJIWONEDA/MadCamp_Project1/assets/133734191/a5add5f3-0cc0-412f-aa2f-512489678412" alt="Screen_Recording_20240703_1401561-ezgif com-video-to-gif-converter" width="180" style="margin-right: 30px;">
    <img src="https://github.com/JANGJIWONEDA/MadCamp_Project1/assets/133734191/af395c11-66e3-4047-990c-453c1b96f532" alt="Screen_Recording_20240703_140403-ezgif com-video-to-gif-converter" width="180" style="margin-right: 30px;">
</div>  

### Index
- [Tab 1](#Tab-1)
- [Tab 2](#Tab-2)
- [Tab 3](#Tab-3)
- [어플리케이션의 구조](#Tab-4)

### Tab 1
: 인연 저장소, Tab 1 구현 설명
<div style="display: flex; flex-wrap: nowrap; justify-content: center;">
    <img src="https://github.com/JANGJIWONEDA/MadCamp_Project1/assets/133734191/44201ea5-81a7-4cdf-894e-fe3c519c2a47" alt="Screen_Recording_20240703_140043-ezgif com-video-to-gif-converter" width="180" style="margin-right: 30px;">
</div>

Tab1의 연락처에서는 `recyclerView`를 사용합니다. 

먼저 연락처 접근 허용을 받습니다. 이후 입력하거나, 불러오는 형식으로 연락처를 추가할 수 있습니다.

Tab3에서 입력했거나, 불러온 연락처는 json 파일에 저장되고, 각 연락처 `cardView` 형식으로 `recyclerView` 위에 표시 됩니다. 마찬가지로 상단에 있는 `search_view` 는 이름을 통해서 원하는 연락처를 찾을 수 있도록 돕습니다. `filter` 를 통해서 매칭 되도록 구현하였습니다.


### Tab 2
: 추억 저장소, Tab 2 구현 설명
<div style="display: flex; flex-wrap: nowrap; justify-content: center;">
    <img src="https://github.com/JANGJIWONEDA/MadCamp_Project1/assets/133734191/a5add5f3-0cc0-412f-aa2f-512489678412" alt="Screen_Recording_20240703_1401561-ezgif com-video-to-gif-converter" width="180" style="margin-right: 30px;">
</div>

Tab2의 갤러리는 `gridView` 로 이루어져 있습니다.  

먼저 갤러리 권한 허용을 받고, 갤러리에서 이미지를 선택하면 `dialog`가 팝업 됩니다. 해당 `dialog`에서 이미지 이름(tag)을 설정하게 되면, `gridView`에 이미지가 추가 됩니다. 이 이미지는 json 파일에 저장됩니다.

추가된 이미지를 클릭하면, `dialog` 를 통해서 이미지의 이름을 확인해 볼 수 있습니다.

상단에 있는 `search_view` 는 이미지 이름을 통해서 원하는 이미지를 찾을 수 있도록 돕습니다. `filter` 를 통해서 매칭 되도록 구현하였습니다.

마지막으로 `latice`(혹은 `list`) 모양의 toggle 버튼은 ‘보기’(latice 버튼을 누르면 `gridView` 보기가 4열 보기로 바뀌고, list 모양 버튼을 누르면 `gridView` 보기가 1열 보기로  바뀌게 됩니다)를 바꿔 줍니다.  Button 모양은 `selector.xml`을 만들어서 설정해 보았습니다.

### Tab 3
: 여행의 기록, Tab 3 구현 설명
<div style="display: flex; flex-wrap: nowrap; justify-content: center;">
    <img src="https://github.com/JANGJIWONEDA/MadCamp_Project1/assets/133734191/af395c11-66e3-4047-990c-453c1b96f532" alt="Screen_Recording_20240703_140403-ezgif com-video-to-gif-converter" width="180" style="margin-right: 30px;">
</div>

Tab3의 '여행 기록'은 `viewPager`로 이루어져 있습니다. 다음은 각 Fragment에 관한 설명입니다.

  
Fragment1: Fragment 1에서는 `recyclerView`를 사용합니다. Tab1에 저장되어 있는 연락처 중 '여행 기록'의 이름과 일치하는 것들 찾아서 `recyclerView`에 보여줍니다. 

Fragment2: Fragment 2에서는 `gridView`를 사용합니다. Tab2에 저장되어 있는 사진 중 '여행 기록'의 이름과 일치하는 것들 찾아서 `gridView`에 보여줍니다. Tab2에서와 마찬가지로  latice(혹은 list) 모양의 toggle 버튼을 지원합니다.

Fragment2: Fragment 3에서는 `recyclerView`를 사용합니다. Tab3에서 입력한 Memo는 `RoomDB`에 저장되고, 각 Memo는 `cardView` 형식으로 `recyclerView` 위에 표시 됩니다. 마찬가지로 상단에 있는 `search_view` 는 메모 이름, 또는 내용을 통해서 원하는 메모를 찾을 수 있도록 돕습니다. `filter` 를 통해서 매칭 되도록 구현하였습니다.






### Tab 4
  : 어플리케이션의 구
