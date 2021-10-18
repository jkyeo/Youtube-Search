package mobile.tap.youtubesearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import mobile.tap.youtubesearch.repo.SearchService
import mobile.tap.youtubesearch.utils.ResultParser
import mobile.tap.youtubesearch.widget.VideoDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var vLoading: View
    private lateinit var adapter: MultiTypeAdapter

    companion object {
        const val TAG = "SearchYoutube"
    }

    private val searchService = SearchService.getInstance().createSearchService()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        etSearch = findViewById(R.id.et_search)
        recyclerView = findViewById(R.id.recyclerView)
        vLoading = findViewById(R.id.v_loading)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MultiTypeAdapter()
        adapter.register(VideoDelegate())

        recyclerView.adapter = adapter

        etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == IME_ACTION_SEARCH) {
                val keyword = etSearch.text.toString().trim()
                search(keyword)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun search(keyword: String?) {
        if (keyword.isNullOrBlank()) {
            Toast.makeText(this, "Please input keyword.", Toast.LENGTH_SHORT).show()
            return
        }

        val disposable = searchService.search(keyword)
            // exec http request on io thread
            .subscribeOn(Schedulers.io())
            // exec regex express on computation thread
            .observeOn(Schedulers.computation())
            .map {
                it.body()?.string()?.let { result ->
                    ResultParser.parse(result)
                } ?: emptyList()
            }
            // notify adapter on main thread
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                val imm = this.getSystemService(InputMethodManager::class.java)
                imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
                vLoading.visibility = View.VISIBLE
            }
            .subscribe(
                {
                    Log.d(TAG, it.toString())
                    vLoading.visibility = View.GONE
                    adapter.items = it
                    adapter.notifyDataSetChanged()
                    recyclerView.post {
                        recyclerView.scrollToPosition(0)
                    }
                },
                {
                    vLoading.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Something went wrong, please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}