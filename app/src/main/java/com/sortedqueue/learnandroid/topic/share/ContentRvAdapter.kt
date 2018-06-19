package com.sortedqueue.learnandroid.topic.share


import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.view.*
import android.widget.TextView
import com.pddstudio.highlightjs.HighlightJsView
import com.sortedqueue.learnandroid.R

/**
 * Created by Alok on 19/06/18.
 */
class ContentRvAdapter ( private var contentList : ArrayList<String> ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when( viewType ) {
            TYPE_CODE -> CodeViewHolder( LayoutInflater.from(parent!!.context).inflate(R.layout.item_code, parent, false))
            TYPE_HEADER -> HeaderViewHolder( LayoutInflater.from(parent!!.context).inflate(R.layout.item_header, parent, false))
            else -> NormalViewHolder( LayoutInflater.from(parent!!.context).inflate(R.layout.item_normal, parent, false))
        }
    }

    inner class CodeViewHolder(view: View?) : RecyclerView.ViewHolder(view) {

        val highlightJsView : HighlightJsView = view!!.findViewById(R.id.highlightJsView)
        init {

        }
    }

    inner class HeaderViewHolder(view: View?) : RecyclerView.ViewHolder(view) {

        val headerTextView : TextView = view!!.findViewById(R.id.headerTextView)
        init {

        }
    }

    inner class NormalViewHolder(view: View?) : RecyclerView.ViewHolder(view) {

        val sharedEditText : TextView = view!!.findViewById(R.id.sharedEditText)

        init {

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val content = getItemAtPosition( position )
        when( getItemViewType(position) ) {
            TYPE_CODE -> initCodeHolder( holder, content )
            TYPE_HEADER -> initHeaderHolder( holder, content )
            else -> initNormalHolder( holder, content )
        }
    }

    private fun initNormalHolder(holder: RecyclerView.ViewHolder?, content: String) {
        val normalHolder = holder as NormalViewHolder
        normalHolder.sharedEditText.setText( content )
        normalHolder.sharedEditText.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onActionItemClicked(p0: ActionMode?, item: MenuItem?): Boolean {
                val id = item!!.itemId
                val start = normalHolder.sharedEditText.selectionStart
                val end = normalHolder.sharedEditText.selectionEnd
                val wordToSpan = normalHolder.sharedEditText.text as Spannable
                when( id ) {
                    R.id.item_code -> {
                        addCode( content, wordToSpan, start, end )
                        return true
                    }
                    R.id.item_header -> {
                        addHeader( content, wordToSpan, start, end )
                        return true
                    }
                }

                return false
            }

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode!!.title = "Mark as : "
                mode.menuInflater.inflate(R.menu.menu_code, menu);
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, menu: Menu?): Boolean {
                menu!!.removeItem(android.R.id.selectAll);
                // Remove the "cut" option
                menu.removeItem(android.R.id.cut);
                // Remove the "copy all" option
                menu.removeItem(android.R.id.copy);
                return true
            }

            override fun onDestroyActionMode(p0: ActionMode?) {

            }

        }
    }

    private fun addHeader(content: String, wordToSpan: Spannable, start: Int, end: Int) {
        content.removeRange(start, end)
        val head =  "<head>" + wordToSpan.toString().substring(start, end) + "</head>"
        if( contentList.size == 0 ) {
            contentList.add(0, head)
        }
        else {
            contentList.add( contentList.size - 1, head )
        }
        notifyDataSetChanged()
    }

    private fun addCode(content: String,  wordToSpan: Spannable, start: Int, end: Int) {
        content.removeRange(start, end)
        val code =  "<code>" + wordToSpan.toString().substring(start, end) + "</code>"
        contentList.add(code)
        notifyDataSetChanged()
    }

    private fun initHeaderHolder(holder: RecyclerView.ViewHolder?, content: String) {
        val headerHolder = holder as HeaderViewHolder
        headerHolder.headerTextView.text = content
    }

    private fun initCodeHolder(holder: RecyclerView.ViewHolder?, content: String) {
        val codeHolder = holder as CodeViewHolder
        codeHolder.highlightJsView.setShowLineNumbers(true)
        codeHolder.highlightJsView.setSource(content)
    }

    private val TYPE_CODE: Int = 1
    private val TYPE_HEADER: Int = 2
    private val TYPE_NORMAL: Int = 3

    override fun getItemViewType(position: Int): Int {
        val content = getItemAtPosition( position )
        if( content.contains("<code>") ) {
            return TYPE_CODE
        }
        else if( content.contains("<head>") ) {
            return TYPE_HEADER
        }
        else
            return TYPE_NORMAL

    }

    private fun getItemAtPosition(position: Int): String {
        return contentList[position]
    }
}