package com.example.myapplication

import android.Manifest
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.databinding.ActivityMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

//http 응답인 json 파일 파싱을 위한 구조체
data class ResultPath(
    val route : Result_trackoption,
    val message : String,
    val code : Int
)
data class Result_trackoption(
    val traoptimal : List<Result_path>
)
data class Result_path(
    val summary : Result_distance,
    val path : List<List<Double>>
)
data class Result_distance(
    val distance : Int
)
interface NaverAPI{
    @GET("v1/driving")
    fun getPath(
        @Header("X-NCP-APIGW-API-KEY-ID") apiKeyID: String,
        @Header("X-NCP-APIGW-API-KEY") apiKey: String,
        @Query("start") start: String,
        @Query("goal") goal: String,
    ): Call<ResultPath>

}

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMapBinding
    lateinit var mapView: MapView
    lateinit var naverMap: NaverMap
    private val LOCATION_PERMISSTION_REQUEST_CODE: Int = 1000
    private lateinit var locationSource: FusedLocationSource // 위치를 반환하는 구현체
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    //선택된 마커의 리스트 내의 인덱스 저장할 전역변수
    var Chosen_Marker_Index: Int? =null

    var Markerlist =arrayOfNulls<Marker>(4)

    val locationPermissionRequest = registerForActivityResult(//권한 요청용 객체
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            } else -> {
            // No location access granted.
        }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "식당 지도"
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
        //의존성 추가 필요
        locationSource = FusedLocationSource(this, LOCATION_PERMISSTION_REQUEST_CODE)
        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onMapReady(naverMap: NaverMap) {
        //전역변수 naverMap에 NaverMap 객체를 넣음
        this.naverMap = naverMap

        //경로 그리기 응답바디가 List<List<Double>> 이라서 2중 for문 썼음
        val path = PathOverlay()

        lateinit var mk: Marker
        //strings.xml에서 좌표 정보를 가져옴
        val laty:Array<String> = resources.getStringArray(R.array.laty)
        val lngx:Array<String> = resources.getStringArray(R.array.lngx)
        //strings.xml에서 전화번호 배열 가져옴
        val priceList=resources.getStringArray(R.array.price)
        val agency_name=resources.getStringArray(R.array.agency_name)
        var index= 0

        //http 통신 요청 세팅
        val APIKEY_ID="hl2fbfg0t9"
        val APIKEY="nb1RUASRnGdwMkJPxGmiiyWzudFvkwiPURcSnjPy"
        val retrofit = Retrofit.Builder().
        baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/").
        addConverterFactory(GsonConverterFactory.create()).
        build()
        val api = retrofit.create(NaverAPI::class.java)

        //위치 오버레이. 현재 위치에 파란색 동그라미(변경가능). 지도당 하나밖에 생성 x
        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true


        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

        //현재위치로 지도 위치 바꾸기
        naverMap.locationSource = locationSource
        //사용자 위치 따라 오버레이와 지도 이동하기
        naverMap.locationTrackingMode = LocationTrackingMode.Follow


        //마커 클릭이벤트 객체: 마커를 클릭하면 현재 위치부터 마커까지 길찾기 루트 표시
        //마커마다 클릭이벤트로 넣어줘야함 marker.onClickEvent=listener
        //걸리는 시간 표시 추가해야함!!!
        val listener = Overlay.OnClickListener { overlay ->
            val marker = overlay as Marker
            //현재 위치를 locationOverlay로 받아오는건 가능했는데, 이게 움직이면서도 가능한건가?
            //가능함
            val callgetPath = api.getPath(APIKEY_ID, APIKEY,"${locationOverlay.position.longitude},${locationOverlay.position.latitude}", "${marker.position.longitude},${marker.position.latitude}")
            //키아이디, 키, 출발지 위도경도, 도착지 위도경도
            var path_cords_list :List<Result_path>?

            //path 그리기
            callgetPath.enqueue(object : Callback<ResultPath>  {
                override fun onResponse(
                    call: Call<ResultPath>,
                    response: Response<ResultPath>
                ) {

                    //http 요청 의 body 부분 불러오기
                    path_cords_list = response.body()?.route?.traoptimal

                    //MutableList에 add 기능 쓰기 위해 더미 원소 하나 넣어둠
                    val path_container: MutableList<LatLng>? = mutableListOf(LatLng(0.1, 0.1))
                    for (path_cords in path_cords_list!!) {
                        for (path_cords_xy in path_cords?.path!!) {
                            //구한 경로를 하나씩 path_container에 추가해줌
                            path_container?.add(LatLng(path_cords_xy[1], path_cords_xy[0]))
                        }
                    }
                    //더미원소 드랍후 path.coords에 path들을 넣어줌.
                    path.coords = path_container?.drop(1)!!
                    path.color = Color.GREEN
                    path.width=25

                    //길 테두리 설정
                    path.outlineWidth = 5
                    path.outlineColor = Color.WHITE

                    //패스에 가려지는 마커 가리기
//                    path.isHideCollidedMarkers = true
                    //패스에 가려지는 장소 심벌 가리기
                    path.isHideCollidedCaptions = true

                    path.map = naverMap
                    path.patternImage = OverlayImage.fromResource(R.drawable.userdirect)
                    path.patternInterval = 40


                    //경로 시작점으로 화면 이동
                    if (path.coords != null) {
                        val cameraUpdate = CameraUpdate.scrollTo(path.coords[0]!!)
                            .animate(CameraAnimation.Fly, 3000)
                        naverMap!!.moveCamera(cameraUpdate)

                        Toast.makeText(this@MapActivity, "경로 안내가 시작됩니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ResultPath>, t: Throwable) {
                    Toast.makeText(this@MapActivity, "경로 안내 실패", Toast.LENGTH_SHORT).show()
                    //종료
                    TODO("Not yet implemented")
                }
            })

            index=Markerlist.indexOf(marker)
            Chosen_Marker_Index=index
            binding.address.text=agency_name[index]
            binding.price.text = priceList[index]

            true
        }

        //마커 array init
        for(i in 0..3) {
            mk = Marker().apply {
                position = LatLng(laty[i].toDouble(), lngx[i].toDouble())
                map = naverMap
            }
            //위치 심벌(아파트,카페 이름)이 마커에 가린다면 아예 안보이게 하기
            mk.isHideCollidedSymbols = true

            //클릭이벤트 : 클릭된 마커를 목적지로 현재 위치서 길찾기 실행
            mk.onClickListener=listener

            Markerlist[i] = mk
        }

    }

}