package iam.immlkit.facedetector.view.nonfacesimages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import iam.immlkit.facedetector.BR
import iam.immlkit.facedetector.R
import iam.immlkit.facedetector.databinding.FragmentImageListBinding
import iam.immlkit.facedetector.model.ImageModel
import iam.immlkit.facedetector.view.BaseFragment
import iam.immlkit.facedetector.view.imageadapter.ImagesAdapter
import iam.immlkit.facedetector.view.itemdecorations.GridItemDecoration
import iam.immlkit.facedetector.viewmodel.images.NonFacesImagesViewModel
import omeronce.android.onelineradapter.adapters.OneLinerAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class NonFacesImagesFragment: BaseFragment() {

    companion object{

        private val SPANS_COUNT = 3

        fun newInstance(): NonFacesImagesFragment {
            val bundle = Bundle()
            val fragment = NonFacesImagesFragment().apply {
                arguments = bundle
            }
            return fragment
        }
    }

    //lazy inity to view model so it will be immutable
    private val viewModel: NonFacesImagesViewModel by viewModel()

    private lateinit var binding: FragmentImageListBinding
    private val adapter by lazy { OneLinerAdapter.Builder<ImageModel>().withLayout(R.layout.item_image).toVariableId(
        BR.image).withItemClickListener {
        Toast.makeText(activity, it.path, Toast.LENGTH_LONG).show()
    }.setDiffUtil(ImageModel.diffCallback).paged().build() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_list,container,false)
        initRecycler()

        return binding.root
    }

    private fun initRecycler() {
        binding.rvImages.addItemDecoration(GridItemDecoration(resources.getDimension(R.dimen.grid_decorator_space).toInt(),SPANS_COUNT))
        binding.rvImages.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    private fun observeData() {
        viewModel.allNonFacesImages.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }
}