# EcoWise v1.1.0

EcoWise is an eco-friendly lifestyle tracking and product analysis application built with Jetpack Compose, Room, and Supabase.

## In v1.1.0

* **Supabase Authentication Fix**: Resolved data transmission bugs (such as empty password strings) during user sign-up and sign-in.
* **Local Room Database Fallback**: Implemented an offline/local Room database fallback mechanism alongside Supabase for enhanced data validation and user session reliability.
* **Database Schema Initialization**: Added complete SQL initialization scripts (`supabase_schema.sql`) for proper remote user table setup and Row Level Security (RLS) policies.

## Unimplemented / Reserved Features (Code Warnings Context)
The following code structures and methods are currently reserved or unimplemented in this version:
* **User Management:** `updateUser`, `clearUser` (Room database actions).
* **Advanced Operations:** `getCurrentUserId`, `deleteAnalyzedRecords`, `removeFromFavorites`, `insertProductToDb`.
* **Rewards & Products State:** `rewardById`, `selectedReward`, `favoriteProducts`, `setCurrentProduct`.

## Installation
Download the `app-debug.apk` attached to the GitHub Release to test.

## Supabase Configuration
* **Project ID**: `actshpmtaaqxmlofbwrv`
* **Dashboard URL**: [EcoWise Supabase Dashboard](https://supabase.com/dashboard/project/actshpmtaaqxmlofbwrv)
* **SQL Editor**: [Supabase SQL Editor](https://supabase.com/dashboard/project/actshpmtaaqxmlofbwrv/sql/new)
