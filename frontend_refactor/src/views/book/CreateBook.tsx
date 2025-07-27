/** @jsxImportSource @emotion/react */
import * as style from "@/styles/style";
import React, { useEffect, useState } from 'react';
import { useCookies } from 'react-cookie';
import Select from 'react-select';

import { createBook } from '@/apis/book/book';
import { getCategoryTree } from '@/apis/category/category';
import { getPublishers } from '@/apis/publisher/publisher';
import { getAllAuthorsByName } from '@/apis/author/author';

import { BookCreateRequestDto } from '@/dtos/book/request/book-create.request.dto';
import { CategoryTreeResponseDto } from '@/dtos/category/response/category-tree.response.dto';
import { PublisherResponseDto } from '@/dtos/publisher/response/publisher.response.dto';
import { AuthorResponseDto } from '@/dtos/author/response/author.response.dto';
import { PageResponseDto } from '@/dtos/page-response.dto';
import './book.css';

function CreateBook () {
  const [cookies] = useCookies(["accessToken"]);

  const [isbn, setIsbn] = useState("");
  const [bookTitle, setBookTitle] = useState("");
  const [bookPrice, setBookPrice] = useState<number>();
  const [publishedDate, setPublishedDate] = useState("");
  const [coverFile, setCoverFile] = useState<File | null>(null);
  const [pageCount, setPageCount] = useState("");
  const [language, setLanguage] = useState("");
  const [description, setDescription] = useState("");
  
  const [categoryType, setCategoryType] = useState<'DOMESTIC' | 'FOREIGN'>("DOMESTIC");
  const [categoryTree, setCategoryTree] = useState<CategoryTreeResponseDto[]>([]);
  const [categoryId, setCategoryId] = useState<number>();

  const [authorName, setAuthorName] = useState("");
  const [authorOptions, setAuthorOptions] = useState<{ label: string; value: number }[]>([]);
  const [selectedAuthor, setSelectedAuthor] = useState<{ label: string; value: number } | null>(null);

  const [publisherName, setPublisherName] = useState("");
  const [publisherOptions, setPublisherOptions] = useState<{ label: string; value: number }[]>([]);
  const [selectedPublisher, setSelectedPublisher] = useState<{ label: string; value: number } | null>(null);

  useEffect(() => {
    const token = cookies.accessToken;
    if (!token) return;

    const fetchCategoryTree = async () => {
      const res = await getCategoryTree(categoryType, token);
      if (res.code === 'SU' && res.data) {
        const filteredCategory = res.data.filter(data => 
          data.isActive == true
        )
        setCategoryTree(filteredCategory);
      } else {
        alert("카테고리를 불러오지 못했습니다.");
      }
    };

    fetchCategoryTree();
  }, [categoryType]);

  useEffect(() => {
    const token = cookies.accessToken;
    if (!publisherName || !token) return;

    const delayDebounce = setTimeout(async () => {
      const res = await getPublishers(token, publisherName);
      if (res.code === 'SU' && res.data) {
        const publishers = Array.isArray(res.data) ? res.data : (res.data as PageResponseDto<PublisherResponseDto>).content;
        const options = publishers.map((p) => ({ label: p.publisherName, value: p.publisherId }));
        setPublisherOptions(options);
      }
    }, 300);

    return () => clearTimeout(delayDebounce);
  }, [publisherName]);

  useEffect(() => {
  const token = cookies.accessToken;
  if (!authorName || !token) return;

  const delayDebounce = setTimeout(async () => {
    const res = await getAllAuthorsByName(authorName, token);
    if (res.code === 'SU' && res.data) {
      const options = res.data.map((a: AuthorResponseDto) => ({
        label: `${a.authorName} (${a.authorEmail})`,
        value: a.authorId
      }));
      setAuthorOptions(options);
    }
  }, 300);

  return () => clearTimeout(delayDebounce);
}, [authorName]);


  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const token = cookies.accessToken;
    if (!token) {
      alert("로그인이 필요합니다.");
      return;
    }

    try {
      const publisherId = selectedPublisher?.value;
      const authorId = selectedAuthor?.value;
      if (!publisherId) throw new Error("출판사를 선택해 주세요.");
      if (!authorId) throw new Error("저자를 선택해 주세요")

      const dto: BookCreateRequestDto = {
        isbn,
        bookTitle,
        categoryId: categoryId!,
        authorId,
        publisherId,
        bookPrice: bookPrice!,
        publishedDate,
        pageCount,
        language,
        description,
      };

      const res = await createBook(dto, token, coverFile);
      if (res.code !== "SU") return alert(res.message);

      alert("책 등록 성공!");
    } catch (err: any) {
      alert(err.message || "책 등록 실패");
    }
  };


  return (
    <form onSubmit={handleSubmit} className="create-book-form">
      <div style={{display: "flex", justifyContent: "space-between", alignItems: "center", height: 40}}>
        <p css={style.modalTitle}>책 등록</p>
        <button 
          type="submit" 
          css={style.createButton}>등록</button>
      </div>

      <input
        type="text"
        value={isbn} 
        onChange={(e) => setIsbn(e.target.value)} 
        placeholder="책 ISBN" 
        className="create-book-input" 
        required />
      <input
        type="text" 
        value={bookTitle} 
        onChange={(e) => setBookTitle(e.target.value)} 
        placeholder="책 제목" 
        className="create-book-input" 
        required />
      <select 
        value={categoryType} 
        onChange={(e) => setCategoryType(e.target.value as 'DOMESTIC' | 'FOREIGN')} 
        className="select">
        <option value="DOMESTIC">국내도서</option>
        <option value="FOREIGN">해외도서</option>
      </select>
      <select 
        value={categoryId ?? ""} 
        onChange={(e) => setCategoryId(Number(e.target.value))} 
        className="select" 
        required>
        <option value="">카테고리 선택</option>
        {categoryTree.flatMap((parent) =>
          parent.subCategories?.map((child) => (
            <option key={child.categoryId} value={child.categoryId}>
              {parent.categoryName} &gt; {child.categoryName}
            </option>
          )) ?? []
        )}
      </select>
      <Select
        inputValue={authorName}
        onInputChange={(input) => setAuthorName(input)}
        options={authorOptions}
        onChange={(option) => setSelectedAuthor(option)}
        placeholder="저자 입력"
        isClearable
      />
      <Select
        inputValue={publisherName}
        onInputChange={(input) => setPublisherName(input)}
        options={publisherOptions}
        onChange={(option) => setSelectedPublisher(option)}
        placeholder="출판사 입력"
        isClearable
      />
      <input 
        type="number" 
        value={bookPrice ?? ""} 
        onChange={(e) => setBookPrice(Number(e.target.value))} 
        placeholder="가격" 
        className="create-book-input"
        required />
      <input 
        type="date" 
        value={publishedDate} 
        onChange={(e) => setPublishedDate(e.target.value)} 
        placeholder="출판일" 
        className="create-book-input" 
        required />
      <div 
        className="file-upload-wrapper">
        <label htmlFor="coverUpload" className="file-upload-label" style={{ backgroundColor: "#e74c3c"}}>
          책 표지 업로드
        </label>
        <input
          id="coverUpload"
          type="file"
          onChange={(e) => setCoverFile(e.target.files?.[0] ?? null)}
          className="file-upload-input"
          />
        {coverFile && <p className="file-name">선택된 파일: {coverFile.name}</p>}
      </div>


      <input 
        type="text" 
        value={pageCount} 
        onChange={(e) => setPageCount(e.target.value)} 
        placeholder="총 페이지수" 
        className="create-book-input" 
        required />
      <input 
      type="text" 
      value={language} 
      onChange={(e) => setLanguage(e.target.value)} 
      placeholder="언어" 
      className="create-book-input" 
      required />
      <textarea 
      value={description} 
      onChange={(e) => setDescription(e.target.value)} 
      placeholder="설명" 
      className="create-book-input" 
      required />
      
    </form>
  );
}

export default CreateBook;
