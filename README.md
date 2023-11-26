# 소통하는 주거 환경

수행기간: 2021년 3월 15일 → 2021년 12월 6일

# 📌 Summery

코로나19로 인해 집에 있는 시간이 늘어나면서 층간 소음에 관한 문제가 심각해지고 있다는 뉴스를 보고 제작을 결심하게 되었습니다.

안드로이드 아두이노를 활용한 어플리케이션 입니다. 이 과정에서 원하는 기능을 구현 하려면 어떤 센서를 써야하는지 고민해보았던 프로젝트 입니다.

### ⚒ 주요 기능

- 자신이 발생하고 있는 소음의 정도를 메인 화면에서 확인하여, 스스로 소음을 줄이도록 유도합니다.
- 게시판을 통해 같은 공동 주거환경에 거주하는 사람들과 소통할 수 있습니다.
- 관리인과의 1:1채팅기 가능해집니다.
- 특정 기준 이상의 소음과 진동이 발생하면 사용자의 휴대폰으로 알림이 갑니다.

# 🤔 Background

코로나 19로 인해 집에 있는 시간이 늘어남에 따라 층간 소음 문제가 심해지고 있다는 뉴스를 보았습니다. 저 역시 이 문제에 시달리고 있었기에, 같은 어려움을 겪는 사람들에게 도움을 주고자 해당 앱을 기획하게 되었습니다.

단순히 층간 소음을 제어하는 것 뿐 아니라, 주차공간 부족 문제에 대한 해결책도 제시합니다. 특히, 주차 가능한 공간을 신속하게 찾아내기 위해 각 주차장에 주차 가능한 차량 수를 표시하는 기능을 구상하였습니다. 그러나 넓은 주차장에 센서를 일일이 설치하는 것은 비용이 많이 들 것이라 판단하였고, 이를 위해 초음파 센서를 활용하여 주차장 입구에 설치해 차량의 출입 횟수를 카운트하고 주차 가능한 차량 수를 측정하는 방식을 도입하였습니다.

또한, '전화공포증'을 가진 사람들이 점차 증가하고 있는 현대사회를 고려하여 관리인과의 소통을 원활하게 할 수 있는 1:1 채팅 기능을 추가하였습니다. 이를 통해 사용자들이 관리인과 쉽고 간편하게 소통할 수 있도록 하였습니다.

# 🔎 Meaning

이 프로젝트를 진행하면서 처음으로 팀원들 간의 의견 충돌을 경험하게 되었습니다. 특히 층간소음을 측정하는 센서의 위치에 대한 부분에서는 많은 의견 차이가 있었습니다. 저는 공동 주거 환경의 다양한 내구성을 고려하여 아랫집의 천장에 센서를 설치하는 것이 가장 적절하다고 판단하였습니다. 이에 대해 팀원들의 동의를 얻기 위해 그림을 그려 설명하는 등 여러 방법을 사용하였습니다. 이 과정에서 의견을 명확하고 논리적으로 제시하는 것의 중요성을 깨달았습니다.

또한, 기능을 구상한 후에 어떤 센서를 선택해야 하는지에 대한 고민을 통해 센서의 다양한 종류를 알게 되었습니다. 이를 통해 SW 개발자이더라도 HW에 대한 지식이 있다면 개발 과정에서 큰 도움이 된다는 것을 깨닫게 되었습니다.

사용자 데이터를 효율적으로 관리하기 위해 Android의 SharedPreference를 활용하여 개발을 진행하였습니다. 이를 통해 사용자의 아이디나 이름뿐만 아니라 동/호수와 같은 다양한 데이터를 저장하고 전달하는 방법을 배웠습니다. 또한, 메인 화면에서 일정 시간마다 데이터를 변경하는 로직을 구현하여 실시간으로 데이터를 가져오는 기능을 개발해봄으로써 안드로이드 개발에 대한 이해도를 더욱 높일 수 있었습니다.

# 🔨 Technology Stack(s)

<table>
<tr>
 <td align="center">언어</td>
 <td>
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">

 </td>
</tr>
<tr>
 <td align="center">프레임워크</td>
 <td>
  <img src="https://img.shields.io/badge/android studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=ffffff"/>
</tr>

<tr>
 <td align="center">데이터 베이스</td>
 <td>
    <img src="https://img.shields.io/badge/firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=white">

  </td>
</tr>

<tr>
 <td align="center">하드웨어</td>
 <td>
    <img src="https://img.shields.io/badge/arduino-00878F?style=for-the-badge&logo=arduino&logoColor=white">

  </td>
</tr>

<tr>
 <td align="center">패키지 매니저</td>
 <td>
    <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">

  </td>
</tr>

</table>

# 시연 영상

https://github.com/thisiseunjin/happyHome/assets/98890934/1f01ddca-d3f3-4b25-af0a-fdb07d76c2e1
