"use client";

import Link from "next/link";
import Image from "next/image";

export default function ProfilePage() {
  return (
    <div className="p-8">
      {/* 프로필 이미지 및 기본 정보 */}
      <div className="flex flex-col items-center mb-8">
        <div className="relative w-32 h-32 mb-4">
          <Image
            src="/profile-default.png"
            alt="프로필 이미지"
            fill
            className="rounded-full object-cover border-4 border-purple-200"
          />
          <button
            className="absolute bottom-0 right-0 bg-purple-500 text-white p-2 rounded-full hover:bg-purple-600 transition-colors"
            onClick={() => {
              // TODO: 이미지 변경 로직 구현
            }}
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-5 w-5"
              viewBox="0 0 20 20"
              fill="currentColor"
            >
              <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
            </svg>
          </button>
        </div>
        <h2 className="text-2xl font-bold mb-2">프로토일 앨범다</h2>
        <p className="text-gray-600 mb-4">proto@example.com</p>
      </div>

      <div className="space-y-8">
        {/* 기본 정보 섹션 */}
        <section className="bg-purple-50 rounded-lg p-6">
          <h2 className="text-xl font-semibold mb-4">기본 정보</h2>
          <div className="space-y-2">
            <Link
              href="/user/profile/edit"
              className="flex items-center justify-between p-4 hover:bg-purple-100 rounded-lg transition-colors"
            >
              <div className="flex items-center gap-3">
                <span className="text-lg">프로필 수정</span>
              </div>
              <span className="text-gray-400">›</span>
            </Link>
          </div>
        </section>

        {/* 연결된 서비스 섹션 */}
        <section className="bg-purple-50 rounded-lg p-6">
          <h2 className="text-xl font-semibold mb-4">연결된 서비스</h2>
          <div className="space-y-2">
            <div className="flex items-center justify-between p-4 hover:bg-purple-100 rounded-lg transition-colors">
              <div className="flex items-center gap-3">
                <svg
                  viewBox="0 0 24 24"
                  className="w-6 h-6 text-[#1DB954]"
                  fill="currentColor"
                >
                  <path d="M12 0C5.4 0 0 5.4 0 12s5.4 12 12 12 12-5.4 12-12S18.66 0 12 0zm5.521 17.34c-.24.359-.66.48-1.021.24-2.82-1.74-6.36-2.101-10.561-1.141-.418.122-.779-.179-.899-.539-.12-.421.18-.78.54-.9 4.56-1.021 8.52-.6 11.64 1.32.42.18.479.659.301 1.02zm1.44-3.3c-.301.42-.841.6-1.262.3-3.239-1.98-8.159-2.58-11.939-1.38-.479.12-1.02-.12-1.14-.6-.12-.48.12-1.021.6-1.141C9.6 9.9 15 10.561 18.72 12.84c.361.181.54.78.241 1.2zm.12-3.36C15.24 8.4 8.82 8.16 5.16 9.301c-.6.179-1.2-.181-1.38-.721-.18-.601.18-1.2.72-1.381 4.26-1.26 11.28-1.02 15.721 1.621.539.3.719 1.02.419 1.56-.299.421-1.02.599-1.559.3z" />
                </svg>
                <span className="text-lg">Spotify</span>
              </div>
              <span className="text-green-500">연결됨</span>
            </div>
          </div>
        </section>
      </div>
    </div>
  );
}
