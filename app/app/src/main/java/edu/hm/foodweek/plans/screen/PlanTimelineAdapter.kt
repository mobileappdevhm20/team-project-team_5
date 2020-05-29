package edu.hm.foodweek.plans.screen

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.github.vipulasri.timelineview.TimelineView
import edu.hm.foodweek.R
import edu.hm.foodweek.plans.persistence.model.WeekDay
import kotlinx.android.synthetic.main.day_meals_timeline_entry.view.*

data class PlanTimelineItem(
    val day: WeekDay,
    val dishImageURL: String,
    val breakfastTitle: String,
    val lunchTitle: String,
    val dinnerTitle: String
)

class PlanTimelineAdapter(
    private val mFeedList: List<PlanTimelineItem>,
    val request: RequestBuilder<Drawable>
) : RecyclerView.Adapter<PlanTimelineAdapter.PlanTimelineViewHolder>() {
    private lateinit var mLayoutInflater: LayoutInflater

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanTimelineViewHolder {
        if (!::mLayoutInflater.isInitialized) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }

        return PlanTimelineViewHolder(
            mLayoutInflater.inflate(
                R.layout.day_meals_timeline_entry,
                parent,
                false
            ), viewType
        )
    }

    override fun onBindViewHolder(holder: PlanTimelineViewHolder, position: Int) {
        val day = mFeedList[position]
        holder.dayTitle.text = day.day.name

        request.load(day.dishImageURL)
            .placeholder(R.drawable.ic_launcher_foreground)
            .priority(Priority.HIGH)
            .transition(withCrossFade(1000))
            .circleCrop()
            .into(holder.imageView)

        holder.breakfastTitle.text = day.breakfastTitle
        holder.lunchTitle.text = day.lunchTitle
        holder.dinnerTitle.text = day.dinnerTitle
    }

    override fun getItemCount() = mFeedList.size

    inner class PlanTimelineViewHolder(itemView: View, viewType: Int) :
        RecyclerView.ViewHolder(itemView) {

        val dayTitle = itemView.day_text
        val imageView = itemView.imageView
        val breakfastTitle = itemView.breakfast_text
        val lunchTitle = itemView.lunch_text
        val dinnerTitle = itemView.dinner_text
        val timeline = itemView.timeline

        init {
            timeline.initLine(viewType)
        }
    }

}