package com.example.petmarket2020.Controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petmarket2020.Adapters.LoadMoreHorizontalAdapter;
import com.example.petmarket2020.Adapters.RV_FavoritesAdapter;
import com.example.petmarket2020.Adapters.RV_PetCategoryAdapter;
import com.example.petmarket2020.Adapters.RV_PostManageAdapter;
import com.example.petmarket2020.Adapters.RV_PosterAdapter;
import com.example.petmarket2020.Adapters.RV_RateAdapter;
import com.example.petmarket2020.Adapters.SliderAdapter;
import com.example.petmarket2020.Adapters.items.PosterItem;
import com.example.petmarket2020.DAL.PostDAL;
import com.example.petmarket2020.DAL.UsersDAL;
import com.example.petmarket2020.HelperClass.MyViewPager;
import com.example.petmarket2020.HelperClass.PaginationScrollListener;
import com.example.petmarket2020.HelperClass.PaginationScrollListener2;
import com.example.petmarket2020.HelperClass.Utils;
import com.example.petmarket2020.Interfaces.IControlData;
import com.example.petmarket2020.Models.PetTypeModel;
import com.example.petmarket2020.Models.PostModel;
import com.example.petmarket2020.Models.RankingModel;
import com.example.petmarket2020.Models.SessionManager;
import com.example.petmarket2020.Models.UsersModel;
import com.example.petmarket2020.R;
import com.example.petmarket2020.Views.FlashSellActivity;
import com.example.petmarket2020.Views.LoginActivity;
import com.example.petmarket2020.Views.PostActivity;
import com.example.petmarket2020.Views.PostDetailActivity;
import com.example.petmarket2020.Views.SearchActivity;
import com.google.android.material.snackbar.Snackbar;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PostController {
    private static final String KEY_DOG = "dog", KEY_CAT = "cat";
    private final Activity activity;
    private PostDAL postDAL;
    private UsersDAL usersDAL;
    private final PostController postController;
    private SessionManager sessionManager;

    public PostController(Activity activity) {
        this.activity = activity;
        this.postDAL = new PostDAL();
        this.usersDAL = new UsersDAL(activity);
        this.postController = this;
        sessionManager = new SessionManager(activity);
    }

    private RV_PosterAdapter homeRVPosterAdapter;
    private boolean isLoadingVER;
    private boolean isLastPageVer;

    private LoadMoreHorizontalAdapter loadMoreHorizontalAdapter;
    private boolean isLoadingHor;
    private boolean isLastPageHor;

    public void refreshData() {
        this.postDAL = new PostDAL();
        this.usersDAL = new UsersDAL(activity);
        // refresh data
        // VERTICAL
        homeRVPosterAdapter = null;
        // HORIZONTAL
        loadMoreHorizontalAdapter = null;
        isLoadingVER = isLoadingHor = isLastPageHor = isLastPageVer = false;
    }

    public boolean checkLogin() {
        return usersDAL.userIsExists();
    }

    public void getPetBreeds(String type, RadioGroup radioGroup, MyViewPager vpg, TextView tvTitle) {
        if (!PostActivity.isLoaded()) {
            radioGroup.removeAllViews();
            if (PostActivity.getData(type) != null) {
                initView((Map<Integer, String>) Objects.requireNonNull(PostActivity.getData(type)), radioGroup, tvTitle, vpg);
                return;
            }
            postDAL.getPetBreeds(type, new IControlData() {
                @Override
                public void responseData(Object objData) {
                    if (objData != null) {
                        Map<Integer, String> dataResp = (Map<Integer, String>) objData;
                        if (type.equals(KEY_DOG) && PostActivity.getData(KEY_DOG) == null) {
                            PostActivity.addOrUpdateData(KEY_DOG, dataResp);
                        } else if (type.equals(KEY_CAT) && PostActivity.getData(KEY_CAT) == null) {
                            PostActivity.addOrUpdateData(KEY_CAT, dataResp);
                        }
                        initView(dataResp, radioGroup, tvTitle, vpg);
                    }
                }
            });
        } else {
            if (radioGroup.getChildCount() == 0) {
                radioGroup.removeAllViews();
                initView((Map<Integer, String>) Objects.requireNonNull(PostActivity.getData(type)), radioGroup, tvTitle, vpg);
            }
        }

    }

    public void deleteImageFragment(String imageId) {
        postDAL.deleteImageFragment(imageId);
    }

    private void initView(Map<Integer, String> dataResp, RadioGroup radioGroup, TextView tvTitle, MyViewPager vpg) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        String breed = (String) PostActivity.getData(PostActivity.KEY_BREEDS);
        for (Map.Entry<Integer, String> data : dataResp.entrySet()) {
            @SuppressLint("InflateParams")
            RadioButton radioButton = (RadioButton) activity.getLayoutInflater().inflate(R.layout.radio_button_layout, null);
            setListener(data.getValue(), radioButton, tvTitle, vpg);
            radioButton.setId(data.getKey());
            radioButton.setText(data.getValue());
            if (breed != null && breed.equals(data.getValue())) {
                radioButton.setChecked(true);
            }
            radioButton.setLayoutParams(layoutParams);
            radioGroup.addView(radioButton);
        }
        PostActivity.setLoadingStatus(false);
    }

    private void setListener(final String value, RadioButton radioButton, TextView tvTitle, MyViewPager vpg) {
        radioButton.setOnClickListener(v -> {
            PostActivity.addOrUpdateData(PostActivity.KEY_BREEDS, value);
            int currentIndex = vpg.getCurrentItem();
            vpg.setCurrentItem(currentIndex + 1);
            tvTitle.setText(PostActivity.getTitle(currentIndex + 1));
        });
    }

    public void postUpload(PostModel postModel, Set<String> imageRemoved, HashMap<String, byte[]> mapImage, RelativeLayout rlBar, MyViewPager vpg, TextView tvTitles) {
        rlBar.setVisibility(View.VISIBLE);
        postDAL.postUpload(postModel, mapImage, imageRemoved, new IControlData() {
            @Override
            public void isSuccessful(boolean isSu) {
                if (isSu) {
                    int nextIndex = vpg.getCurrentItem() + 1;
                    vpg.setCurrentItem(nextIndex);
                    tvTitles.setText(PostActivity.getTitle(nextIndex));
                } else {
                    Toast.makeText(activity, "Đã xảy ra lỗi, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
                rlBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void getPetType(RecyclerView rvCategoryDog, RecyclerView rvCategoryCat) {
        rvCategoryCat.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        rvCategoryDog.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        postDAL.getPetType(new IControlData() {
            @Override
            public void responseData(Object objData) {
                Object[] objRs = (Object[]) objData;
                List<PetTypeModel> catList = (List<PetTypeModel>) objRs[0];
                List<PetTypeModel> dogList = (List<PetTypeModel>) objRs[1];
                rvCategoryCat.setAdapter(new RV_PetCategoryAdapter(catList, breed -> {
                    Intent intent = new Intent(activity, SearchActivity.class);
                    intent.putExtra("breed", breed);
                    activity.startActivity(intent);
                }));
                rvCategoryDog.setAdapter(new RV_PetCategoryAdapter(dogList, breed -> {
                    Intent intent = new Intent(activity, SearchActivity.class);
                    intent.putExtra("breed", breed);
                    activity.startActivity(intent);
                }));
                rvCategoryDog.setHasFixedSize(true);
                rvCategoryCat.setHasFixedSize(true);
            }
        });

    }

    public void getPostFilter(RecyclerView rvSearch, String queryArea, String queryPeType, String breed) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        rvSearch.setLayoutManager(gridLayoutManager);
        List<PosterItem> oldList = new ArrayList<>();
        isLoadingHor = true;
        Location location = sessionManager.getLocation();
        LoadMoreHorizontalAdapter.IOnItemClick iOnItemClick = (postId, peType1, price1, views) -> {
            Intent intent = new Intent(activity, PostDetailActivity.class);
            intent.putExtra("postId", postId);
            intent.putExtra("peType", peType1);
            intent.putExtra("price", price1);
            activity.startActivity(intent);
            postDAL.updateViewsCount(postId, views);
        };
        postDAL.getPostFilter(queryArea, queryPeType, location, breed, new IControlData() {
            @Override
            public void responseData(Object data) {
                List<PosterItem> posterItems = (List<PosterItem>) data;
                if (!posterItems.isEmpty()) {
                    oldList.addAll(posterItems);
                    loadMoreHorizontalAdapter = new LoadMoreHorizontalAdapter(posterItems, 1, iOnItemClick);
                    loadMoreHorizontalAdapter.setPostController(postController);
                    loadMoreHorizontalAdapter.setLocationUser(location);
                    if (posterItems.size() == 6) {
                        loadMoreHorizontalAdapter.addItemLoading();
                    } else {
                        loadMoreHorizontalAdapter.removeItemLoading();
                        isLastPageHor = true;
                    }
                    isLoadingHor = false;
                    rvSearch.setAdapter(loadMoreHorizontalAdapter);
                } else {
                    isLastPageHor = true;
                }
            }
        });
        rvSearch.addOnScrollListener(new PaginationScrollListener2(gridLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoadingHor = true;
                postDAL.getPostFilter(queryArea, queryPeType, location, breed, new IControlData() {
                    @Override
                    public void responseData(Object data) {
                        List<PosterItem> itemsLoading = (List<PosterItem>) data;
                        if (!itemsLoading.isEmpty()) {
                            new Handler().postDelayed(() -> {
                                oldList.addAll(itemsLoading);
                                loadMoreHorizontalAdapter.removeItemLoading();
                                loadMoreHorizontalAdapter.updateData(oldList);
                                isLoadingHor = false;
                                if (itemsLoading.size() == 6) {
                                    loadMoreHorizontalAdapter.addItemLoading();
                                } else {
                                    Toast.makeText(activity, "Hết dữ liệu rồi!!", Toast.LENGTH_SHORT).show();
                                    isLastPageHor = true;
                                }
                            }, 1000);
                        } else {
                            loadMoreHorizontalAdapter.removeItemLoading();
                            Toast.makeText(activity, "Hết dữ liệu rồi!!", Toast.LENGTH_SHORT).show();
                            isLastPageHor = true;
                        }
                    }
                });
            }

            @Override
            public boolean isLoading() {
                return isLoadingHor;
            }

            @Override
            public boolean isLastPage() {
                return isLastPageHor;
            }
        });

    }


    public void getPostHome(RecyclerView rvPoster, RelativeLayout rlBar, NestedScrollView nestedScrollView, RecyclerView rvHot) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rlBar.getLayoutParams();
        rlBar.setVisibility(View.VISIBLE);
        rvPoster.setLayoutManager(new GridLayoutManager(activity, 2));
        List<PosterItem> oldListNormal = new ArrayList<>();
        List<PosterItem> oldListHot = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
        rvHot.setLayoutManager(linearLayoutManager);
        LoadMoreHorizontalAdapter.IOnItemClick iOnItemClickHorizontal = (postId, peType1, price1, views) -> {
            Intent intent = new Intent(activity, PostDetailActivity.class);
            intent.putExtra("postId", postId);
            intent.putExtra("peType", peType1);
            intent.putExtra("price", price1);
            activity.startActivity(intent);
            postDAL.updateViewsCount(postId, views);
        };
        RV_PosterAdapter.IOnItemClick iOnItemClick = (postId, peType, price, views) -> {
            Intent intent = new Intent(activity, PostDetailActivity.class);
            intent.putExtra("postId", postId);
            intent.putExtra("peType", peType);
            intent.putExtra("price", price);
            activity.startActivity(intent);
            postDAL.updateViewsCount(postId, views);
        };
        /*
        postDAL.getPostHome(int typeRequest,IPost)
        typeRequest = 1 normalList, = 2 hotList, = 0 cả 2 :_)
         */
        isLoadingVER = true;
        isLoadingHor = true;
        postDAL.getPostHome(0, new IControlData() {
            @Override
            public void responseData(List<PosterItem> listNormal, List<PosterItem> listHot) {
                // Normal
                oldListNormal.addAll(listNormal);
                homeRVPosterAdapter = new RV_PosterAdapter(listNormal, iOnItemClick);
                homeRVPosterAdapter.setPostController(postController);
                homeRVPosterAdapter.setLocationUser(sessionManager.getLocation());
                rvPoster.setAdapter(homeRVPosterAdapter);
                rlBar.setVisibility(View.GONE);
                isLoadingVER = false;
                // Hot
                if (!listHot.isEmpty()) {
                    oldListHot.addAll(listHot);
                    loadMoreHorizontalAdapter = new LoadMoreHorizontalAdapter(oldListHot, 0, iOnItemClickHorizontal);
                    loadMoreHorizontalAdapter.setPostController(postController);
                    loadMoreHorizontalAdapter.setLocationUser(sessionManager.getLocation());
                    loadMoreHorizontalAdapter.addItemLoading();
                    rvHot.setAdapter(loadMoreHorizontalAdapter);
                    isLoadingHor = false;
                } else {
                    isLastPageHor = true;
                }
            }
        });
        // rvPoster
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {
                if (!isLastPageVer) {
                    if (!isLoadingVER) {
                        if (!firstLoad) {
                            firstLoad = true;
                            layoutParams.topMargin = -250;
                        }
                        rlBar.setVisibility(View.VISIBLE);
                        isLoadingVER = true;
                        postDAL.getPostHome(1, new IControlData() {
                            @Override
                            public void responseData(Object objData) {
                                List<PosterItem> posterItems = (List<PosterItem>) objData;
                                if (!posterItems.isEmpty()) {
                                    oldListNormal.addAll(posterItems);
                                    new Handler().postDelayed(() -> {
                                                homeRVPosterAdapter.updateData(oldListNormal);
                                                isLoadingVER = false;
                                                rlBar.setVisibility(View.GONE);
                                            }, 500
                                    );
                                } else {
                                    Toast.makeText(activity, "Hết dữ liệu rồi!!", Toast.LENGTH_SHORT).show();
                                    rlBar.setVisibility(View.GONE);
                                    isLastPageVer = true;
                                }

                            }
                        });
                    }
                }

            }
        });
        // rvHot
        rvHot.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoadingHor = true;
                postDAL.getPostHome(2, new IControlData() {
                    @Override
                    public void responseData(Object objData) {
                        List<PosterItem> posterItems = (List<PosterItem>) objData;
                        if (!posterItems.isEmpty()) {
                            new Handler().postDelayed(() -> {
                                oldListHot.addAll(posterItems);
                                loadMoreHorizontalAdapter.removeItemLoading();
                                loadMoreHorizontalAdapter.updateData(oldListHot);
                                isLoadingHor = false;
                                if (posterItems.size() == 4) {
                                    loadMoreHorizontalAdapter.addItemLoading();
                                } else {
                                    isLastPageHor = true;
                                }
                            }, 1000);
                        } else {
                            loadMoreHorizontalAdapter.removeItemLoading();
                            isLastPageHor = true;
                        }
                    }
                });
            }

            @Override
            public boolean isLoading() {
                return isLoadingHor;
            }

            @Override
            public boolean isLastPage() {
                return isLastPageHor;
            }
        });
    }

    private boolean firstLoad;

    public void postDetail(String postId, PostModel postModel, SliderView imageSlider, TextView[] textViews) {
        /*
        textViews:
        0: title
        1: price
        2: breed
        3: gender
        4: age
        5: inject
        6: healthy
        7: phone
        8: are
         */
        postDAL.postDetail(postId, postModel, new IControlData() {
            @Override
            public void responseData(Object objData) {
                if (postModel != null) {
                    String phoneNumber = (String) objData;
                    String poType = postModel.getPoType().contains("bán") ? "[BÁN] " : "[MUA] ";
                    String title = poType + postModel.getTitle();
                    String[] data = new String[]{title, Utils.formatCurrencyVN(postModel.getPrice()), postModel.getBreed(),
                            postModel.getPeAge(), postModel.getInjectStatus(), postModel.getHealthGuarantee(), phoneNumber, postModel.getArea()};
                    initPostDetailView(data, textViews, postModel.getImages(), imageSlider);
                } else {
                    List<Object> list = (List<Object>) objData;
                    // 0: PostModel 1: phoneNumber
                    PostModel postModel = (PostModel) list.get(0);
                    String phoneNumber = list.get(1).toString();
                    String poType = postModel.getPoType().contains("bán") ? "[BÁN] " : "[MUA] ";
                    String title = poType + postModel.getTitle();
                    List<String> images = postModel.getImages();
                    String[] data = new String[]{title, Utils.formatCurrencyVN(postModel.getPrice()), postModel.getBreed(),
                            postModel.getPeAge(), postModel.getInjectStatus(), postModel.getHealthGuarantee(), phoneNumber, postModel.getArea()};
                    initPostDetailView(data, textViews, images, imageSlider);
                }
            }
        });

    }

    private void initPostDetailView(String[] data, TextView[] textViews, List<String> images, SliderView imageSlider) {
        for (int i = 0; i < data.length; i++) {
            textViews[i].setText(data[i]);
        }
        SliderAdapter sliderAdapter = new SliderAdapter();
        sliderAdapter.setStringList(images);
        imageSlider.setSliderAdapter(sliderAdapter);
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        imageSlider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        imageSlider.setScrollTimeInSec(3);
        imageSlider.startAutoCycle();
    }

    public void isFavorite(String postId, ImageView ivFav) {
        if (usersDAL.userIsExists()) {
            List<String> favorites = usersDAL.getUserSession().getFavorites();
            if (favorites != null && favorites.contains(postId)) {
                ivFav.setImageResource(R.drawable.ic_item_tym_checked);
                ivFav.setTag(R.id.isChecked, "1"); // checked
            } else {
                ivFav.setTag(R.id.isChecked, "0"); //uncheck
            }
        }
    }

    public void setFavorite(ImageView ivFav, ProgressBar pgBar) {
        if (usersDAL.userIsExists()) {
            UsersModel usersModel = usersDAL.getUserSession();
            ivFav.setVisibility(View.INVISIBLE);
            pgBar.setVisibility(View.VISIBLE);
            String uid = usersModel.getUid();
            String postId = (String) ivFav.getTag(R.id.postId);
            Object a = ivFav.getTag(R.id.isChecked);
            if(a==null) return;
            boolean isChecked = !a.toString().equals("0");
            List<String> favorites = usersModel.getFavorites();
            if (favorites == null) favorites = new ArrayList<>();
            if (isChecked)  // da~ luu
                favorites.remove(postId);
            else  // chua luu
                favorites.add(postId);

            usersDAL.setFavorite(uid, favorites, new IControlData() {
                @Override
                public void isSuccessful(boolean isSu) {
                    if (isSu) {
                        pgBar.setVisibility(View.INVISIBLE);
                        ivFav.setVisibility(View.VISIBLE);
                        if (isChecked) {
                            ivFav.setImageResource(R.drawable.ic_item_tym);
                            ivFav.setTag(R.id.isChecked, "0"); //uncheck
                        } else {
                            ivFav.setImageResource(R.drawable.ic_item_tym_checked);
                            ivFav.setTag(R.id.isChecked, "1"); //check
                            Toast.makeText(activity, "Đã thêm vào mục yêu thích", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            showSBMargin(ivFav, "Đăng nhập để tiếp tục lưu tin");
        }

    }

    private void showSBMargin(View v, String content) {
        Snackbar sb = Snackbar.make(v, content, Snackbar.LENGTH_LONG)
                .setAction("ĐĂNG NHẬP", v1 -> activity.startActivity(new Intent(activity, LoginActivity.class)));
        sb.setActionTextColor(Color.CYAN);
        sb.setAnchorView(activity.findViewById(R.id.vTmp));
        sb.show();
    }

    public void getSamePosts(String peType, long price, RecyclerView rvSamePost) {
        List<PosterItem> oldSamePostItemList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
        rvSamePost.setLayoutManager(linearLayoutManager);
        LoadMoreHorizontalAdapter.IOnItemClick iOnItemClick = (postId, peType1, price1, views) -> {
            Intent intent = new Intent(activity, PostDetailActivity.class);
            intent.putExtra("postId", postId);
            intent.putExtra("peType", peType1);
            intent.putExtra("price", price1);
            activity.startActivity(intent);
            activity.finish();
            postDAL.updateViewsCount(postId, views);
        };
        postDAL.getSamePosts(peType, price, new IControlData() {
            @Override
            public void responseData(Object objData) {
                List<PosterItem> posterItems = (List<PosterItem>) objData;
                if (!posterItems.isEmpty()) {
                    oldSamePostItemList.addAll(posterItems);
                    loadMoreHorizontalAdapter = new LoadMoreHorizontalAdapter(posterItems, 0, iOnItemClick);
                    loadMoreHorizontalAdapter.setPostController(postController);
                    loadMoreHorizontalAdapter.setLocationUser(sessionManager.getLocation());
                    if (posterItems.size() == 4) {
                        loadMoreHorizontalAdapter.addItemLoading();
                    } else {
                        loadMoreHorizontalAdapter.removeItemLoading();
                        isLastPageHor = true;
                    }
                    rvSamePost.setAdapter(loadMoreHorizontalAdapter);
                } else {
                    isLastPageHor = true;
                }
            }
        });
        rvSamePost.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoadingHor = true;
                postDAL.getSamePosts(peType, price, new IControlData() {
                    @Override
                    public void responseData(Object objData) {
                        List<PosterItem> itemsLoading = (List<PosterItem>) objData;
                        if (!itemsLoading.isEmpty()) {
                            new Handler().postDelayed(() -> {
                                oldSamePostItemList.addAll(itemsLoading);
                                loadMoreHorizontalAdapter.removeItemLoading();
                                loadMoreHorizontalAdapter.updateData(oldSamePostItemList);
                                isLoadingHor = false;
                                if (itemsLoading.size() == 4) {
                                    loadMoreHorizontalAdapter.addItemLoading();
                                } else {
                                    Toast.makeText(activity, "Hết dữ liệu rồi!!", Toast.LENGTH_SHORT).show();
                                    isLastPageHor = true;
                                }
                            }, 1000);
                        } else {
                            Toast.makeText(activity, "Hết dữ liệu rồi!!", Toast.LENGTH_SHORT).show();
                            loadMoreHorizontalAdapter.removeItemLoading();
                            isLastPageHor = true;
                        }
                    }
                });
            }

            @Override
            public boolean isLoading() {
                return isLoadingHor;
            }

            @Override
            public boolean isLastPage() {
                return isLastPageHor;
            }
        });
    }

    public void rankingProcess(String postId, EditText etComment, RatingBar ratingUser, View pgBar2, Button btnSubmit, TextView tvTime) {
        UsersModel usersModel = usersDAL.getUserSession();
        if (usersModel != null) {
            String uId = usersModel.getUid();
            btnSubmit.setVisibility(View.INVISIBLE);
            pgBar2.setVisibility(View.VISIBLE);
            String comment = etComment.getText().toString();
            int rate = (int) ratingUser.getRating();
            String time = Utils.getCurrentDate(true);
            postDAL.rankingProcess(postId, comment, rate, time, uId, new IControlData() {
                @Override
                public void isSuccessful(boolean isSu) {
                    if (isSu) {
                        btnSubmit.setVisibility(View.VISIBLE);
                        pgBar2.setVisibility(View.INVISIBLE);
                        tvTime.setText(time);
                        updateViewRate(btnSubmit, ratingUser, etComment);
                        Toast.makeText(activity, "Đã gửi đánh giá của bạn", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity, "Đã xảy ra lỗi, zui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            showSBMargin(btnSubmit, "Đăng nhập để tiếp tục bình luận & đánh giá");
        }
    }

    private void updateViewRate(Button btn, RatingBar ratingBar, EditText editText) {
        ratingBar.setIsIndicator(true);
        editText.setTag(editText.getText().toString());
        editText.setEnabled(false);
        btn.setText(PostDetailActivity.EDIT);
        btn.setBackgroundResource(R.drawable.bg_fill_circle_edit);
    }

    public void isRatedAndGetAllComment(String postId, RelativeLayout rlIsRate, Button btn, RatingBar ratingUser
            , EditText etComment, TextView tvTime, RatingBar ratingBarTotal, TextView tvBarTotal, RecyclerView rvRate) {
        UsersModel usersModel = usersDAL.getUserSession();
        if (usersModel != null) {
            String uId = usersModel.getUid();
            postDAL.isRated(postId, uId, new IControlData() {
                @Override
                public void responseData(Object data) {
                    if (data != null) {
                        rlIsRate.setVisibility(View.VISIBLE);
                        RankingModel rankingModel = (RankingModel) data;
                        ratingUser.setRating(rankingModel.getRate());
                        tvTime.setText(rankingModel.getTime());
                        etComment.setText(rankingModel.getComment());
                        updateViewRate(btn, ratingUser, etComment);
                    }
                }
            });
        }
        postDAL.getAllComment(postId, new IControlData() {
            @Override
            public void responseData(Object data) {
                String totalString = "(chưa có)";
                if (data != null) {
                    Object[] objRsp = (Object[]) data;
                    List<RankingModel> rankingModelList = (List<RankingModel>) objRsp[0];
                    List<String[]> nameAndAvatarList = (List<String[]>) objRsp[1];
                    float averageStar;
                    long totalStar;
                    long size = rankingModelList.size();
                    if (size != 0) {
                        totalStar = rankingModelList.stream().mapToLong(RankingModel::getRate).sum();
                        averageStar = (float) totalStar / (float) size;
                        ratingBarTotal.setRating(averageStar);
                        totalString = "(" + ((double) Math.round(averageStar * 10) / 10) + ")";
                        tvBarTotal.setText(totalString);
                        // Set commentList adapter
                        // Nếu người dùng đã đăng nhập, xóa trong rv thâu
                        if (usersModel != null) {
                            for (int i = 0; i < rankingModelList.size(); i++) {
                                if (rankingModelList.get(i).getUserId().equals(usersModel.getUid())) {
                                    rankingModelList.remove(i);
                                    nameAndAvatarList.remove(i);
                                    break;
                                }
                            }
                        }
                        RV_RateAdapter rv_rateAdapter = new RV_RateAdapter(rankingModelList, nameAndAvatarList);
                        rvRate.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
                        rvRate.setHasFixedSize(true);
                        rvRate.setAdapter(rv_rateAdapter);
                    } else {
                        tvBarTotal.setText(totalString);
                    }
                } else {
                    tvBarTotal.setText(totalString);
                }

            }
        });
    }

    // PostManae Controller
    public SessionManager.PostManageItemCount getPostOfUserSession() {
        return usersDAL.getPostOfUserSession();
    }

    public void getPostByStatus(long status, RecyclerView recyclerView, View pgBar, IControlData iControlDataView) {
        // status: 0 Đang duyệt, 1 OK, 2 Bị từ chấu, 3 ẩn cmn danh
        UsersModel usersModel = usersDAL.getUserSession();
        if (usersModel != null) {
            int viewType = 0; // Dang ban
            if (status == 0) viewType = 3; //Đang duyệt
            else if (status == 2) viewType = 2; //Bị từ chấu
            else if (status == 3) viewType = 1; // An~
            String uId = usersModel.getUid();
            pgBar.setVisibility(View.VISIBLE);

            RV_PostManageAdapter postManageAdapter = new RV_PostManageAdapter(viewType, new RV_PostManageAdapter.IOnItemClick() {
                @Override
                public void onClick(PostModel postModel, int type) {
                    if (type == RV_PostManageAdapter.FLASH) {
                        Intent intent = new Intent(activity, FlashSellActivity.class);
                        intent.putExtra("image", postModel.getImages().get(0));
                        intent.putExtra("title", postModel.getTitle());
                        intent.putExtra("time", postModel.getTimeStart());
                        intent.putExtra("postId", postModel.getPostId());
                        activity.startActivity(intent);
                    } else if (type == RV_PostManageAdapter.ITEM_CLICK) {
                        Intent intent = new Intent(activity, PostDetailActivity.class);
                        intent.putExtra("postModel", postModel);
                        activity.startActivity(intent);
                    } else if (type == RV_PostManageAdapter.EDIT) {
                        Intent intent = new Intent(activity, PostActivity.class);
                        intent.putExtra("postModel", postModel);
                        activity.startActivity(intent);
                    } else {
                        pgBar.setVisibility(View.VISIBLE);
                        boolean isHide = type == RV_PostManageAdapter.HIDDEN;
                        postDAL.hideOrUnHidePost(isHide, postModel.getPostId(), new IControlData() {
                            @Override
                            public void isSuccessful(boolean isSu) {
                                pgBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(activity, isHide ? "Đã ẩn tin" : "Tin đã được hiển thị", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
            postDAL.getPostByStatus(uId, status, new IControlData() {
                @Override
                public void responseData(Object data) {
                    List<PostModel> postModelList = (List<PostModel>) data;
//                    Log.e("StatusHIHE", status + " - - - Size " + postModelList.size());
                    postManageAdapter.setOrUpdateData(postModelList);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(postManageAdapter);
                    iControlDataView.responseData(postModelList.size());
                    pgBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    //Favorites
    public void getFavoriteList(RecyclerView recyclerView) {
        UsersModel usersModel = usersDAL.getUserSession();
        if (usersModel != null) {
            RV_FavoritesAdapter rv_favoritesAdapter = new RV_FavoritesAdapter((price, peType, postId) -> {
                Intent intent = new Intent(activity, PostDetailActivity.class);
                intent.putExtra("postId", postId);
                intent.putExtra("peType", peType);
                intent.putExtra("price", price);
                activity.startActivity(intent);
            });
            rv_favoritesAdapter.setPostController(postController);
            postDAL.getFavoriteList(usersModel.getUid(), new IControlData() {
                @Override
                public void responseData(Object data) {
                    List<PosterItem> posterItems = (List<PosterItem>) data;
                    rv_favoritesAdapter.setData(posterItems);
                    recyclerView.setAdapter(rv_favoritesAdapter);
                }
            });
        }
    }
}