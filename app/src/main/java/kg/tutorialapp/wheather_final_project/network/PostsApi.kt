package kg.tutorialapp.wheather_final_project.network

import kg.tutorialapp.wheather_final_project.Post
import retrofit2.Call
import retrofit2.http.*

interface PostsApi {

    @GET("posts/{id}")
    fun fetchPostById(
            @Path("id") id: Int
    ): Call<Post>

    @POST("posts")
    fun createPost(
            @Body post: Post
    ): Call<Post>

    @POST("posts")
    @FormUrlEncoded
    fun createPostUsingFields(
            @Field("userId") userId: String,
            @Field("title") title: String,
            @Field("body") body: String
    ): Call<Post>

    @POST("posts")
    @FormUrlEncoded
    fun createPostUsingFieldsMap(
            @FieldMap map: HashMap<String, String>
    ): Call<Post>
}