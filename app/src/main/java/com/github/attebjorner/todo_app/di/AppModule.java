package com.github.attebjorner.todo_app.di;

import android.content.Context;

import com.github.attebjorner.todo_app.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule
{
    @Singleton
    @Provides
    public App provideApp(@ApplicationContext Context app)
    {
        return (App) app;
    }
}
