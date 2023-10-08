import React from 'react';
import { TailSpin } from 'react-loader-spinner';
import { Link } from 'react-router-dom';
import { useGeAllCategories } from '../hooks/useGetAllCategories';

const Categories = () => {
  const { allCategories, isLoading } = useGeAllCategories();

  if (isLoading)
    return (
      <div className='flex items-center justify-center h-[200px] w-full'>
        <TailSpin width='20px' height='20px' color='gray' />
      </div>
    );
  return (
    <aside className='p-5 border-[1px] flex flex-col'>
      <h3 className='font-semibold text-slate-900 text-[15px] md:text-[17px] mb-3 md:mb-5 '>
        Categories
      </h3>
      {allCategories.map(({ name }, index) => (
        <Link
          to={`/blogs?category=${name?.replaceAll(' ', '-')}`}
          className={`uppercase  w-full font-[600]  px-2 py-1 hover:bg-gray-600 hover:text-white transition-all duration-300  md:px-3 md:py-2 ${
            index % 2 !== 0
              ? 'bg-white text-gray-700'
              : 'bg-gray-200 text-gray-700'
          } `}
          key={name}
        >
          {name}
        </Link>
      ))}
    </aside>
  );
};

export default Categories;
