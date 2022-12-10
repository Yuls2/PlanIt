package com.planitse2022.planit.util.retrofit;

import com.planitse2022.planit.data.AccountData;
import com.planitse2022.planit.data.BackgroundData;
import com.planitse2022.planit.data.ChecklistData;
import com.planitse2022.planit.data.GroupData;
import com.planitse2022.planit.data.GroupRequestData;
import com.planitse2022.planit.data.MemberData;
import com.planitse2022.planit.data.NoticeData;
import com.planitse2022.planit.data.PlantData;
import com.planitse2022.planit.data.PostData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.TestData;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitAPI {
    //Http Request용 메서드 선언
    /* some examples
    @GET("users/{user}/repos") -> @Path: {user}가 user로 대체
    Call<List<Repo>> listRepos(@Path("user") String user);

    @GET("group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort); -> @Query: 파라미터

    @FormUrlEncoded
    @POST("user/edit")
    Call<User> updateUser(@Field("first_name") String first, @Field("last_name") String last);

    @FormUrlEncoded
    @POST("/getplaninfo.php")
    Call<PlanItem> postSampleData(@FieldMap HashMap<String, Object> param);

    @Multipart
    @PUT("user/photo")
    Call<User> updateUser(@Part("photo") RequestBody photo, @Part("description") RequestBody description);

     */


    //알림 관련 ====================================================

    @POST("getUserNoticeList.php")
    Call<ResponseData<List<NoticeData>>> getUserNoticeList();

    //체크리스트 관련 ====================================================

    @POST("getUserCheckListList.php")
    Call<ResponseData<List<ChecklistData>>> getUserCheckList();

    @FormUrlEncoded
    @POST("getUserGroupCheckList.php")
    Call<ResponseData<ChecklistData>> getUserGroupCheckList(@Field("groupID") int groupID);

    @FormUrlEncoded
    @POST("editGoalName.php")
    Call<ResponseData<Integer>> updateGoalName(@Field("groupID") int groupID, @Field("goalName") String goalName);

    @FormUrlEncoded
    @POST("switchChecked.php")
    Call<ResponseData<Integer>> updateCheckedOfCheckItem(@Field("checkID") int checkID, @Field("isChecked") int isChecked);

    @FormUrlEncoded
    @POST("deleteCheckItem.php")
    Call<ResponseData<Integer>> deleteCheckItem(@Field("checkID") int checkID);

    @FormUrlEncoded
    @POST("insertCheckItem.php")
    Call<ResponseData<Integer>> insertCheckItem(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("editCheckItem.php")
    Call<ResponseData<Integer>> editCheckItem(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("deleteGroup.php")
    Call<ResponseData<Integer>> deleteGroup(@Field("groupID") int groupID);

    //포스트 관련 ====================================================
    @POST("getPostList.php")
    Call<ResponseData<List<PostData>>> getMainPostList();

    @FormUrlEncoded
    @POST("getGroupPostList.php")
    Call<ResponseData<List<PostData>>> getGroupPostList(@Field("groupID") int groupID);

    @FormUrlEncoded
    @POST("insertPost.php")
    Call<ResponseData<Integer>> uploadPost(@Field("groupID") int groupID, @Field("checklistJSON") String checklistJSON
            , @Field("comment") String comment, @Field("b64image") String b64image);

    @FormUrlEncoded
    @POST("insertWater.php")
    Call<ResponseData<Integer>> waterPost(@Field("groupID") int groupID, @Field("postID") int postID);

    //그룹 관련 ====================================================
    @POST("getUserGroupList.php")
    Call<ResponseData<List<GroupData>>> getMyGroupList();

    @FormUrlEncoded
    @POST("insertGroupRequest.php")
    Call<ResponseData<Integer>> insertGroupRequest(@Field("groupID") int groupID, @Field("introduction") String introduction);

    @FormUrlEncoded
    @POST("insertGroupRequest.php")
    Call<ResponseData<Integer>> insertGroupRequest(@Field("groupID") int groupID);

    @FormUrlEncoded
    @POST("getSearchGroupList.php")
    Call<ResponseData<List<GroupData>>> getGroupList(@Field("searchString") String searchString, @Field("maxnum") int maxnum);

    @FormUrlEncoded
    @POST("getGroupInfo.php")
    Call<ResponseData<GroupData>> getGroupInformation(@Field("groupID") int groupID);

    @FormUrlEncoded
    @POST("getIsManager.php")
    Call<ResponseData<Boolean>> getIsManager(@Field("groupID") int groupID);

    @FormUrlEncoded
    @POST("getGroupMemberList.php")
    Call<ResponseData<List<MemberData>>> getGroupMemberList(@Field("groupID") int groupID);

    @FormUrlEncoded
    @POST("getGroupMember.php")
    Call<ResponseData<MemberData>> getGroupMember(@Field("targetUserID") String userID, @Field("groupID") int groupID);

    @FormUrlEncoded
    @POST("getGroupRequestList.php")
    Call<ResponseData<List<GroupRequestData>>> getGroupRequestList(@Field("groupID") int groupID);

    @FormUrlEncoded
    @POST("handleGroupRequest.php")
    Call<ResponseData<Integer>> handleGroupRequest(@Field("accept") int accept, @Field("targetUserID") String userID, @Field("groupID") int groupID);

    @FormUrlEncoded
    @POST("deleteMember.php")
    Call<ResponseData<Integer>> deleteMember(@Field("targetUserID") String userID, @Field("groupID") int groupID);

    @POST("json/backgroundInfo.json")
    Call<List<BackgroundData>> getBackgroundList();

    @FormUrlEncoded
    @POST("updateGroupBackground.php")
    Call<ResponseData<Integer>> updateGroupBackground(@Field("background") int background, @Field("groupID") int groupID);

    @FormUrlEncoded
    @POST("insertGroup.php")
    Call<ResponseData<Integer>> insertGroup(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("insertGroup.php")
    Call<ResponseData<Integer>> editGroup(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("updateGroupMaxNumber.php")
    Call<ResponseData<Integer>> updateGroupMaxNumber(@Field("maxNumber") int maxNumber, @Field("groupID") int groupID);

    @FormUrlEncoded
    @POST("updateUserGroupStatus.php")
    Call<ResponseData<Integer>> updateUserGroupStatus(@Field("groupID") int groupID, @Field("status") String status);

    @FormUrlEncoded
    @POST("leaveGroup.php")
    Call<ResponseData<Integer>> leaveGroup(@Field("groupID") int groupID, @Field("plantDelete") int plantDelete);

    //테스트 ====================================================
    @FormUrlEncoded
    @POST("insertSampleJWT.php")
    Call<ResponseData<Integer>> createGroup(@Field("groupName") String groupName, @Field("groupComment") String groupComment, @Field("isAutoAccept") int isAutoAccept);

    @POST("test.php")
    Call<String> getTest();

    @FormUrlEncoded
    @POST("/getOneDataSample.php")
    Call<TestData> getSampleData(@Field("parameter") int param);

    //식물 관련
    @FormUrlEncoded
    @POST("getPlantInfo.php")
    Call<ResponseData<PlantData>> getPlantInfo(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("getUserPlantList.php")
    Call<ResponseData<List<PlantData>>> getUserPlantList(@Field("mode") int mode);

    @FormUrlEncoded
    @POST("updatePlantType.php")
    Call<ResponseData<Integer>> updatePlantType(@Field("plantID") int plantID, @Field("plantType") int plantType);

    @FormUrlEncoded
    @POST("insertPlant.php")
    Call<ResponseData<Integer>> insertPlant(@FieldMap HashMap<String, Object> param);

    //로그인
    @FormUrlEncoded
    @POST("login.php")
    Call<AccountData> requestLogin(@Field("userID") String userID, @Field("userPass") String userPass);

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseData<Integer>> requestRegister(@FieldMap HashMap<String, Object> param);

}
