SELECT * FROM books;

SELECT
            b.book_isbn,
            b.book_title,
            a.author_name,
            p.publisher_name,
            bc.category_name,
            b.cover_url,
            SUM(cod.amount) AS total_sales
        FROM customer_order_details cod
                 JOIN customer_orders co ON cod.customer_order_id = co.customer_order_id
                 JOIN books b ON cod.book_isbn = b.book_isbn
                 JOIN authors a ON b.author_id = a.author_id
                 JOIN publishers p ON b.publisher_id = p.publisher_id
                 JOIN book_categories bc ON b.category_id = bc.category_id
                 LEFT JOIN refund_orders r ON r.customer_order_id = co.customer_order_id
        WHERE r.customer_order_id IS NULL
        GROUP BY b.book_isbn, b.book_title, a.author_name,
                 p.publisher_name, bc.category_name, b.cover_url
        ORDER BY total_sales DESC
            LIMIT 100;