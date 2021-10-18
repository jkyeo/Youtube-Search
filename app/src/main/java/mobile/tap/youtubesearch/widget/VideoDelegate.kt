package mobile.tap.youtubesearch.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import mobile.tap.youtubesearch.R

class VideoDelegate: ItemViewDelegate<String, VideoDelegate.ViewHolder>() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onBindViewHolder(holder: ViewHolder, item: String) {
        holder.webView.apply {
            // for youtube video play
            this.settings.javaScriptEnabled = true
            this.loadUrl("https://www.youtube.com/embed/$item")
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_item_video, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val webView: WebView = itemView.findViewById(R.id.webView)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.webView.stopLoading()
        holder.webView.loadUrl("about:blank")
        super.onViewRecycled(holder)
    }
}