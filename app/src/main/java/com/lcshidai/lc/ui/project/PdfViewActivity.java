package com.lcshidai.lc.ui.project;

import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.shockwave.pdfium.PdfDocument;
import com.socks.library.KLog;
import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PdfViewActivity extends TRJActivity implements View.OnClickListener {

    @Bind(R.id.ib_top_bar_back)
    ImageButton topBackBtn;
    @Bind(R.id.tv_top_bar_title)
    TextView topTitleText;
    @Bind(R.id.tv_top_bar_right_action)
    TextView topActionText;
    @Bind(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @Bind(R.id.pdfView)
    PDFView pdfView;
    @Bind(R.id.activity_pdf_view)
    LinearLayout activityPdfView;
    @Bind(R.id.pb_load_progress)
    ProgressBar pbLoadProgress;
    @Bind(R.id.tv_progress)
    TextView tvProgress;
    @Bind(R.id.ll_load_tip_container)
    LinearLayout llLoadTipContainer;
    private String pdfFileTitle, pdfFileUrl;

    private static final int DOWN_FIRST = 0;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;

    public static final String SAMPLE_FILE = "sample.pdf";
    private int pageNumber = 0;

    private int progress;
    private String pdfFileDir;
    private String pdfFileSavedName;
    private Thread downLoadThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        ButterKnife.bind(this);

        initViewsAndEvents();
    }


    private void initViewsAndEvents() {
        pdfFileTitle = getIntent().getStringExtra(PublicShowMaterialActivity.PDF_FILE_TITLE);
        pdfFileUrl = getIntent().getStringExtra(PublicShowMaterialActivity.PDF_FILE_URL);
        topBackBtn.setOnClickListener(this);
        topTitleText.setText(pdfFileTitle);
        llLoadTipContainer.setVisibility(View.GONE);
        downLoadThread = new Thread(downloadFileRunnable);
        downLoadThread.start();
    }

    /**
     * pdf下载线程
     */
    private Runnable downloadFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(pdfFileUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Accept-Encoding", "identity");
                conn.connect();
                int progressCount = conn.getContentLength();
                InputStream is = conn.getInputStream();
                pdfFileDir = mContext.getExternalFilesDir(null) + "Download" + File.separator;
//                pdfFileDir = Environment.getExternalStorageDirectory().getPath() + "/trj/Download/";
                File pdfFileDirFile = new File(pdfFileDir);
                if (!pdfFileDirFile.exists()) {
                    pdfFileDirFile.mkdirs();
//                    pdfFileDirFile.createNewFile();
                }
                pdfFileSavedName = pdfFileDir + pdfFileTitle;
                File pdfFile = new File(pdfFileSavedName);
                FileOutputStream fos = new FileOutputStream(pdfFile);
                downloadHandler.sendEmptyMessage(DOWN_FIRST);
                int count = 0;
                byte buf[] = new byte[1024];
                long startTime = 0;
                long endTime = 0;
                int numRead = 0;
                int tempProgress = 0;
                while (true) {
                    numRead = is.read(buf);
                    if (numRead > 0) {
                        count += numRead;
                        tempProgress = (int) (((float) count / progressCount) * 100);
                        endTime = System.currentTimeMillis();
                        if (endTime - startTime >= 500 && tempProgress > progress) {
                            startTime = endTime;
                            progress = tempProgress;
                            downloadHandler.sendEmptyMessage(DOWN_UPDATE);
                        }
                        fos.write(buf, 0, numRead);
                    } else {
                        downloadHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                }

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    Handler downloadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_FIRST:
                    KLog.d("handleMessage", "开始加载");
                    llLoadTipContainer.setVisibility(View.VISIBLE);
                    break;
                case DOWN_UPDATE:
                    llLoadTipContainer.setVisibility(View.VISIBLE);
                    pbLoadProgress.setProgress(progress);
                    break;

                case DOWN_OVER:
                    KLog.d("handleMessage", "加载完成");
                    llLoadTipContainer.setVisibility(View.GONE);
                    Uri uri = Uri.parse(pdfFileSavedName);
                    pdfView.fromUri(uri)
                            .enableSwipe(true)
                            .enableDoubletap(true)
                            .defaultPage(0)
                            .onDraw(new OnDrawListener() {
                                @Override
                                public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                                }
                            })
                            .onLoad(new OnLoadCompleteListener() {
                                @Override
                                public void loadComplete(int nbPages) {
                                    PdfDocument.Meta meta = pdfView.getDocumentMeta();
                                    KLog.e("loadComplete", "title = " + meta.getTitle());
                                    KLog.e("loadComplete", "author = " + meta.getAuthor());
                                    KLog.e("loadComplete", "subject = " + meta.getSubject());
                                    KLog.e("loadComplete", "keywords = " + meta.getKeywords());
                                    KLog.e("loadComplete", "creator = " + meta.getCreator());
                                    KLog.e("loadComplete", "producer = " + meta.getProducer());
                                    KLog.e("loadComplete", "creationDate = " + meta.getCreationDate());
                                    KLog.e("loadComplete", "modDate = " + meta.getModDate());
                                    printBookmarksTree(pdfView.getTableOfContents(), "-");
                                }
                            })
                            .onPageChange(new OnPageChangeListener() {
                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    pageNumber = page;
                                    setTitle(String.format("%s %s / %s", pdfFileTitle, page, pageCount));
                                }
                            })
                            .onError(new OnErrorListener() {
                                @Override
                                public void onError(Throwable t) {
                                    KLog.e("onError", t.getMessage());
                                }
                            })
                            .enableAnnotationRendering(false)
                            .password(null)
                            .load();
                    break;
            }
        }
    };

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            KLog.e("printBookmarksTree", String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                onBackPressed();
                break;
        }
    }
}
