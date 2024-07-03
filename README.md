# "여행 기록지"

여행 기록지는 여행을 보조하거나 기록할 수 있는 어플 입니다. 여행 기록지와 함께 잊지 못할 여행을 떠나보세요!!

## Info

Team: KAIST 19' 김동근 , DGIST 22' 장지원

Duration: 2024.06.26 ~ 2024.07.03

At: KAIST Mad Camp

## Language / IDE

Language: Kotlin

IDE: Android Studio

## Introduction / Implementation

<p>저희 어플은 여행의 기록을 메모할 수 있는 어플입니다.</p>   


**“인연 저장소”**

인연 저장소는 당신의 여행을 함께한, 그리고 함께할 소중한 인연들을 연락처로 확인해볼 수 있는 공간입니다.

**“추억 저장소”**

추억 저장소는 당신의 여행 중에 있었던, 그리고 여행 중에 남기고 싶은 소중한 추억들을 사진으로 담아보세요.

**“여행의 기록”**

[여행의 기록]에서 당신의 여행을 추가하고 확인해 보세요.


  
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
  - [인연 저장소](#Contacts-structure)
  - [추억 저장소](#Image-structure)
  - [여행의 기록](#Diary-structure)

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
![Screen_Recording_20240703_194146 1](https://github.com/JANGJIWONEDA/MadCamp_Project1/assets/85298954/fa5b7077-eafb-4130-9231-688ad27c3a8d)
</div>

Tab3의 '여행의 기록'은 `viewPager`로 이루어져 있습니다. 다음은 각 Fragment에 관한 설명입니다.

  
Fragment1: Fragment 1에서는 `recyclerView`를 사용합니다. Tab1에 저장되어 있는 연락처 중 '여행 기록'의 이름과 일치하는 것들 찾아서 `recyclerView`에 보여줍니다. 

Fragment2: Fragment 2에서는 `gridView`를 사용합니다. Tab2에 저장되어 있는 사진 중 '여행 기록'의 이름과 일치하는 것들 찾아서 `gridView`에 보여줍니다. Tab2에서와 마찬가지로  latice(혹은 list) 모양의 toggle 버튼을 지원합니다.

Fragment2: Fragment 3에서는 `recyclerView`를 사용합니다. Tab3에서 입력한 Memo는 `RoomDB`에 저장되고, 각 Memo는 `cardView` 형식으로 `recyclerView` 위에 표시 됩니다. 마찬가지로 상단에 있는 `search_view` 는 메모 이름, 또는 내용을 통해서 원하는 메모를 찾을 수 있도록 돕습니다. `filter` 를 통해서 매칭 되도록 구현하였습니다.






### Tab 4
  : 어플리케이션의 구조
  ![스크린샷 2024-07-03 182551](https://github.com/JANGJIWONEDA/MadCamp_Project1/assets/85298954/fa849e11-6906-46ee-adbc-2a8f3a3d5a9f)
저희 앱의 전체적인 탭과 데이터 사이의 관계도는 위와 같같습니다

#### Contacts structure
"인연 저장소" 탭
![스크린샷 2024-07-03 182643](https://github.com/JANGJIWONEDA/MadCamp_Project1/assets/85298954/66c28eb8-2173-4253-9cf1-e08abc493af9)
인연 저장소 탭에서는 Contacts.json 파일에서 저장된 이름, 연락처, 관계, 함께가는 여행지등을 불러오고, 또 저장할 수 있습니다.
이 때 diary 데이터에서 여행의 기록에서 등록한 여행지들을 불러와 자동완성 기능을 제공합니다. 여행의 기록에서는 여행지를 통해 연관 연락처들을 가져오기 때문에
오타로 인한 연동 실패를 막을 수 있습니다.

#### Image structure
"추억 저장소" 탭
![스크린샷 2024-07-03 182704](https://github.com/JANGJIWONEDA/MadCamp_Project1/assets/85298954/6397d1bd-e975-4d08-a492-3f4a54f3dae3)
추억 저장소 탭은 인연저장소와 거의 비슷합니다. Images.json 파일에서 저장된 이름과 설명등을 가져오고, 또 저장할 수 있습니다.
사진 역시 diary 데이터에서 가져온 여행지 목록을 이용해 자동완성기능을 제공하며, 이를 통해 여행의 기록 탭에서 관련 사진들을 확인 할 수 있습니다.

#### Diary-structure
"여행의 기록" 탭
![스크린샷 2024-07-03 182726](https://github.com/JANGJIWONEDA/MadCamp_Project1/assets/85298954/a024160f-ab2c-4d58-a2ce-db11a4bc39a5)
여행의 기록 탭에는 해당 여행지의 이름과 태그를 저장할 수 있습니다. 이를 통해 Contacts.json과 Images.json 파일에 저장돼 있는 데이터 중 같은 여행지 태그를 가지고 있는 연락처와 사진만을 모아서 보여줍니다. 또한, 해당 탭에서는 메모를 불러오고 저장할 수 있는데, 기록에 부여된 고유한 id를 통해 해당 기록에서 쓰여진 메모만을 판별하고 보여줍니다. 이 id는 diary가 삭제될 때 까지 바꿀 수 없습니다. 따라서, 어떤 기록에서 쓰여진 메모들은 해당 기록에서만 확인할 수 있습니다. 
