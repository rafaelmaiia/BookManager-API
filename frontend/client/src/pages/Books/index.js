import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiPower, FiEdit, FiTrash2 } from 'react-icons/fi';

import api from '../../services/api';

import './styles.css';
import logoImage from '../../assets/rm_logo.png';

export default function Books() {

    const [books, setBooks] = useState([]);
    const [page, setPage] = useState(1);

    const username = localStorage.getItem('username');
    const accessToken = localStorage.getItem('accessToken');

    const navigate = useNavigate();

    async function logout() {
        localStorage.clear();
        navigate('/');
    }

    async function editBook(id) {
        try {
            navigate(`/book/new/${id}`)
        } catch (error) {
            alert('Edit book failed! Try again.')
        }
    }

    async function deleteBook(id) {
        try {
            await api.delete(`api/book/v1/${id}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            })

            setBooks(books.filter(book => book.id !== id))
        } catch (err) {
            alert('Delete failed! Try again.')
        }
    }

    async function fetchMoreBooks() {
        const response = await api.get('api/book/v1', {
            headers: {
                Authorization: `Bearer ${accessToken}`
            },
            params: {
                page: page,
                size: 4,
                direction: 'asc'
            }
        });

        console.log(response.data); // Adiciona este log para verificar a resposta da API

        if(!response.data._embedded) return;
        setBooks([ ...books, ...response.data._embedded.bookVOList]);
        setPage(prevPage => prevPage + 1);
    }

    useEffect(() => {
        fetchMoreBooks();
    }, []);

    return (
        <div className="book-container">
            <header>
                <img src={logoImage} alt="Erudio" />
                <span>Welcome, <strong>{username.toUpperCase()}</strong>!</span>
                <Link className='button' to="/book/new/0">Add New Book</Link>
                <button onClick={logout} type='button'>
                    <FiPower size={18} color='#251FC5' />
                </button>
            </header>

            <h1>Registered Books</h1>
            <ul>
                {books.map(book => (
                    <li key={book.id}>
                    <strong>Title:</strong>
                    <p>{book.title}</p>
                    <strong>Author:</strong>
                    <p>{book.author}</p>
                    <strong>Price:</strong>
                    <p>{Intl.NumberFormat('pt-BR', {style: 'currency', currency: 'BRL'}).format(book.price)}</p>
                    <strong>Release Date:</strong>
                    <p>{Intl.DateTimeFormat('pt-BR').format(new Date(book.launchDate))}</p>

                    <button onClick={() => editBook(book.id)} type='button'>
                        <FiEdit size={20} color='#251FC5' />
                    </button>
                    <button onClick={() => deleteBook(book.id)} type='button'>
                        <FiTrash2 size={20} color='#251FC5' />
                    </button>
                </li>
                ))}
            </ul>

            <button className="button" onClick={fetchMoreBooks} type='button'>Load More</button>
        </div>
    );
}