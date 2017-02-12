package com.eg.addtendencebackup;

import android.os.AsyncTask;

/**
 * Created by Eugene Galkine on 2/6/2017.
 */

public class WaitToRestart extends AsyncTask<Integer,Void,Integer>
{
    MainActivity context;

    WaitToRestart(MainActivity ctx)
    {
        context = ctx;
    }

    @Override
    protected Integer doInBackground(Integer... params)
    {
        try
        {
            Thread.sleep(params[0]);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println("end");
        return 1;
    }

    @Override
    protected void onPostExecute(Integer result)
    {
        System.out.println("post");
        context.clearScanned();
    }
}
