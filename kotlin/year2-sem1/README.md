# Smart Waste Sorting System - Year 2 Sem 1 (Kotlin)

An Android application that helps users classify product packaging, locate nearby recycling centres, track eco-challenges, and earn rewards for sustainable habits — built with Jetpack Compose, Room, and Supabase.

## Features

- **Product Analysis** — Users log a product's category, brand, weight, packaging material, and eco labels; the app classifies it and stores the result for later review in an analysis history and favourites list
- **Recycling Guide & Centre Locator** — A categorized recycling guide (plastic, glass, paper, metal, e-waste, etc.) paired with a Google Maps–powered screen that plots nearby recycling centres, filterable by accepted material type
- **Eco Challenges & Progress Tracking** — Users can join eco-challenges and track completion progress, backed by both a remote Supabase table (`challenge_progress_table`) and a local Room database for offline reliability
- **Rewards & User Statistics** — A rewards shop lets users redeem points earned from challenges, with a dedicated statistics screen summarizing personal eco-impact
- **Authentication with Local Fallback** — Sign-up/sign-in is handled through Supabase Auth, with a local Room database fallback for session validation and offline resilience
- **Modular Screen Architecture** — Features are organized into self-contained modules built entirely with Jetpack Compose

## Project Structure

```
smart_waste_system/
├── MainActivity.kt                          # App entry point and navigation host
├── module_1_EcoScanner/                     # Product analysis: input form, result, history, favourites
├── module_2_RecyclingGuide/                 # Recycling guide + Google Maps recycling centre locator
├── module_3_EcoChallenge/                   # Eco-challenge listing and progress tracking
├── module_4_EcoStatistics/                  # Rewards shop and user statistics
├── UserModule/                              # Account, login, and profile screens
├── GuidelinesScreen/                        # Onboarding guidelines and About Us screen
├── components/                              # Shared Compose UI components (product cards, gauges, scaffold)
├── ComposableFunction_self_defined/         # Custom reusable Compose elements (buttons, text fields, icons)
├── RoomDB/                                  # Local Room database — entities, DAOs, and offline fallback logic
├── network/                                 # Supabase client and remote services (User, Product, Reward, Challenge)
├── model/                                   # Shared data models
├── ui/theme/                                # App-wide Compose theming (color, typography)
└── data/EcoWiseRepository.kt                # Repository layer bridging Room and Supabase data sources
```

## How to Run

```bash
# Open in Android Studio
File → Open → select the project root folder

# Build & run
Run ▶ on an emulator or physical device
```

Alternatively, download the pre-built `app-debug.apk` attached to the GitHub Release to test without building from source.

## Configuration

### Supabase
- **Project ID:** `xnxhpmrcvkuumwttpqjq`
- **Dashboard URL:** [Supabase Dashboard](https://supabase.com/dashboard/project/xnxhpmrcvkuumwttpqjq)
- **SQL Editor:** [Supabase SQL Editor](https://supabase.com/dashboard/project/xnxhpmrcvkuumwttpqjq/sql/new)

### Google Maps
- Requires a **Google Maps SDK for Android** API key set in `AndroidManifest.xml` (`com.google.android.geo.API_KEY`), used to power the recycling centre locator.

## What I Learned

Building this project gave me practice architecting an Android app around a hybrid data layer — using Supabase as the remote source of truth while falling back to a local Room database for offline reliability and session validation. It also involved integrating a third-party SDK (Google Maps) securely (restricting the API key to a specific package + signing certificate), structuring a multi-module Jetpack Compose app around distinct feature areas, and writing a `supabase_schema.sql` initialization script with proper Row Level Security (RLS) policies for a production-style remote database.
