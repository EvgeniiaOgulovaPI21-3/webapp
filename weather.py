import streamlit as st
import requests
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from datetime import datetime

# Функция для получения данных о погоде (прогноз на несколько дней)
def get_weather_forecast(city, api_key):
    url = f"http://api.openweathermap.org/data/2.5/forecast?q={city}&appid={api_key}&units=metric&lang=ru"
    response = requests.get(url)
    data = response.json()
    
    if data['cod'] == '200':
        return data
    else:
        st.error("Не удалось получить данные. Проверьте правильность введенного города.")
        return None

def add_weekday_in_russian(df):
    df['Дата'] = pd.to_datetime(df['datetime']).dt.strftime('%d.%m.%Y')  
    df['День недели'] = pd.to_datetime(df['datetime']).dt.day_name(locale='ru_RU')  
    df['Время суток'] = pd.to_datetime(df['datetime']).dt.hour
    df['Время суток'] = df['Время суток'].apply(lambda x: 'Утро' if 6 <= x < 12 else ('День' if 12 <= x < 18 else ('Вечер' if 18 <= x < 23 else 'Ночь')))
    return df

# Функция для агрегирования данных по дню и времени суток
def aggregate_weather_data(df):
    grouped_df = df.groupby(['Дата', 'День недели', 'Время суток']).agg(
        Температура=('temp', 'mean'),
        Влажность=('humidity', 'mean'),
        Скорость_ветра=('wind_speed', 'mean')
    ).reset_index()
    
    grouped_df['Температура'] = grouped_df['Температура'].round(1)
    grouped_df['Скорость_ветра'] = grouped_df['Скорость_ветра'].round(1)
    grouped_df['Влажность'] = grouped_df['Влажность'].round().astype(int)  
    
    grouped_df['Температура'] = grouped_df['Температура'].astype(str) + ' °C'
    grouped_df['Влажность'] = grouped_df['Влажность'].astype(str) + ' %'
    grouped_df['Скорость_ветра'] = grouped_df['Скорость_ветра'].astype(str) + ' м/с'
    
    time_order = ['Утро', 'День', 'Вечер', 'Ночь']
    grouped_df['Время суток'] = pd.Categorical(grouped_df['Время суток'], categories=time_order, ordered=True)
    grouped_df = grouped_df.sort_values(by=['Дата', 'Время суток'])
    
    return grouped_df

st.title('Прогноз погоды на несколько дней')
city = st.text_input('Введите название города', 'Москва')
api_key = 'yourApiKey'

if st.button('Получить прогноз погоды'):
    weather_data = get_weather_forecast(city, api_key)
    
    if weather_data:
        st.subheader(f"Прогноз погоды для {city.capitalize()} на несколько дней:")
        forecast_list = weather_data['list']
        
        weather_df = pd.DataFrame([{
            'datetime': item['dt_txt'],
            'temp': item['main']['temp'],
            'humidity': item['main']['humidity'],
            'wind_speed': item['wind']['speed'],
            'weather_description': item['weather'][0]['description']
        } for item in forecast_list])

        weather_df = add_weekday_in_russian(weather_df)
        grouped_df = aggregate_weather_data(weather_df)
        st.subheader("Прогноз погоды")
        st.dataframe(grouped_df.reset_index(drop=True))  
        st.subheader("Графики погоды на несколько дней")

        # График температуры
        fig, ax = plt.subplots(figsize=(12, 6))
        ax.plot(grouped_df['Дата'].astype(str) + ' ' + grouped_df['Время суток'].astype(str), 
                grouped_df['Температура'].str.replace(' °C', '').astype(float), 
                marker='o', color='skyblue', label='Температура (°C)')
        ax.set_title('Температура по дням и времени суток', fontsize=16)
        ax.set_xlabel('Дата и Время суток', fontsize=12)
        ax.set_ylabel('Температура (°C)', fontsize=12)
        ax.tick_params(axis='x', rotation=45)  
        ax.grid(True)
        st.pyplot(fig)

        # График влажности
        fig, ax = plt.subplots(figsize=(12, 6))
        ax.plot(grouped_df['Дата'].astype(str) + ' ' + grouped_df['Время суток'].astype(str), 
                grouped_df['Влажность'].str.replace(' %', '').astype(float), 
                marker='o', color='lightgreen', label='Влажность (%)')
        ax.set_title('Влажность по дням и времени суток', fontsize=16)
        ax.set_xlabel('Дата и Время суток', fontsize=12)
        ax.set_ylabel('Влажность (%)', fontsize=12)
        ax.tick_params(axis='x', rotation=45)  
        ax.grid(True)
        st.pyplot(fig)

        # График скорости ветра
        fig, ax = plt.subplots(figsize=(12, 6))
        ax.plot(grouped_df['Дата'].astype(str) + ' ' + grouped_df['Время суток'].astype(str), 
                grouped_df['Скорость_ветра'].str.replace(' м/с', '').astype(float), 
                marker='o', color='orange', label='Скорость ветра (м/с)')
        ax.set_title('Скорость ветра по дням и времени суток', fontsize=16)
        ax.set_xlabel('Дата и Время суток', fontsize=12)
        ax.set_ylabel('Скорость ветра (м/с)', fontsize=12)
        ax.tick_params(axis='x', rotation=45)  
        ax.grid(True)
        st.pyplot(fig)
