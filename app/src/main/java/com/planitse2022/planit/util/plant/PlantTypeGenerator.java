package com.planitse2022.planit.util.plant;

import java.io.*; // 파일 입출
import java.util.*; // 자료구조 사용
import java.util.Map.Entry;

// json
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PlantTypeGenerator {
    // 식물 키워드 저장을 위한 변수 추가
    // key는 키워드(String), Value는 식물 타입 num(한개의 키워드 당 여러개의 중복되지 않는 타입에 배정될 수 있으므로, Set)
    private Map<String, Set<Integer>> keywordMap = new HashMap<>();

    // 식물 타입 num에 매칭되는 string (e.g. 0:"공부", 1: "과제", ...)
    private Map<Integer, String> plantTypeMap = new HashMap<>();

    public void printKeywords(){
        for (Entry<String, Set<Integer>> entrySet : keywordMap.entrySet()) {
            System.out.print(entrySet.getKey() + " : " + entrySet.getValue()+", ");
        }
        System.out.println();
    }

    // seed 디폴트 값
    public int determintPlantType(String checkListName) {
        long seed = 0;
        return determintPlantType(checkListName, seed);
    }

    // 클래스 다이어그램에서 메서드 추가, 식물 종류 결정 기능
    public int determintPlantType(String checkListName, long seed) {

        // 1. 식물 타입 후보 선정
        Set<Integer> candidatePlantType = new HashSet<Integer>(); // 식물 타입 num 후보
        for(Entry<String, Set<Integer>> entrySet : keywordMap.entrySet()){ // 식물 키워드 각각에 대해 반복
            if(checkListName.contains(entrySet.getKey())){ // 키워드가 있으면
                candidatePlantType.addAll(entrySet.getValue());// 후보 추가
            }
        }

        // 2. 식물 타입 후보 중 랜덤 하나 값 선정
        Random random = new Random(seed); // seed 값으로 랜덤 객체 생성

        if(candidatePlantType.isEmpty()) return random.nextInt(plantTypeMap.size()); // 만약 후보가 없다면 전체에서 랜덤 선택

        // 후보가 있다면
        ArrayList<Integer> candidatePlantTypeArr = new ArrayList<Integer>(candidatePlantType); // 인덱스 호출이 불가능한 Set에서 Array로 형변
        Integer randomNum = random.nextInt(candidatePlantTypeArr.size()); // 랜덤 변수 생성
        return candidatePlantTypeArr.get(randomNum);
    }

    // 식물 타입 num에 매칭되는 string 반환
    public String getPlantTypeByNum(Integer i){
        return plantTypeMap.get(i);
    }

    // json 파일 읽어서 키워드 맵에 저장하는 함수
    public void readKeywordJson(InputStream inputStream) {
        try{
            // 파일 읽기
            // Reader reader = new FileReader(jsonPath); // utf-8 한글이깨져서 사용안함.
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

            // parsing
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(br);

            // 값 읽기 & 맵에 저장
            Iterator<String> keys =  jsonObject.keySet().iterator(); // key값들을 모두 얻어옴.

            Integer plantTypeNum = 0; // 식물 타입에 매칭되는 정수 값
            String plantType; // 식물 타입 하나 저장할 변수
            Set<Integer>plantTypeSet; // 맵에 저장하기 위해 임시로 타입 셋을 가리킬 변수
            JSONArray plantKeywords; // 식물 키워드를 하나를 저장할 변수

            // 파일 내 키값(=식물 타입 값) 있는 동안 반복
            //{0=경찰사법, 1=예술, 2=교육, 3=사회과학, 4=이과, 5=법과, 6=경영, 7=공과, 8=종교, 9=문과, 10=건강, 11=과제, 12=의학, 13=공부}
            while(keys.hasNext()){
                plantType = keys.next(); // JSON의 키값 (의미: 식물 타입 값) 저장
                this.plantTypeMap.put(plantTypeNum, plantType);
                plantKeywords = (JSONArray) jsonObject.get(plantType); // JSON의 value 값(의미: 식물 키워드 값) 저장

                // 식물 키워드 각각에 대해 반복
                for(int i = 0;i<plantKeywords.size();i++){
                    String plantKeyword = plantKeywords.get(i).toString(); // i째 식물 키워드의 값
                    if (this.keywordMap.containsKey(plantKeyword)){ // 이미 존재하는 식물 키워드라면
                        this.keywordMap.get(plantKeyword).add(plantTypeNum); // map의 원래 있던 키워드의 hash set에 새로운 식물 타입 정수 값 추가
                    }
                    else{ // 새로운 식물 키워드라면
                        plantTypeSet = new HashSet<Integer>(); // 새로운 해시 셋 생성
                        plantTypeSet.add(plantTypeNum); // 해시 셋에 식물 타입 정수 값 추가
                        this.keywordMap.put(plantKeyword, plantTypeSet); // map에 새로운 키워드 추가
                    }
                }
                plantTypeNum++; // 카운트 값 증가
            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
//    public static void main(String args[]) {
//
//        String checkItemName = "과학 숙제"; // 체크 아이템 이름
//
//        PlantSystem plantSystem = new PlantSystem();
//        plantSystem.readKeywordJson("src/keyword.json");
//
//        long seed = 3; // 시드 값
//        Integer plantTypeNum = plantSystem.determintPlantType(checkItemName, seed); // 식물 타입 숫자 반환
//        String plantType = plantSystem.getPlantTypeByNum(plantTypeNum); // 식물 타입 숫자 -> 스트링 변환
//
//        for(int i = 0; i<14;i++){
//            System.out.print("["+i + "]"+plantSystem.getPlantTypeByNum(i)+", ");
//        }
//        System.out.println();
//
//        plantSystem.printKeywords();
//
//        // 결과 출력
//        System.out.println("체크 아이템 이름: "+checkItemName);
//        System.out.println("식물 타입 숫자/식물 타입 이름: "+plantTypeNum.toString() + "/" +plantType);
//
//
//    }
}
