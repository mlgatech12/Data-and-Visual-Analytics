ó
@łwZc           @   s_   d  d l  Z  d  d l Z d  d l Z d  d l Z d  d l Z d   Z e d k r[ e   n  d S(   i˙˙˙˙Nc          C   sV  d }  d } d } d } t  j |   \ } } } } t j | |  } | j | |  t j |  }	 t  j |	 | |  }
 t  j |
 |  i  } t d d  E } x; | D]3 } | j	   j
 d  } t | | d | d	 f <q¨ WWd  QXd } t d d  T } xJ | D]B } | j	   j
 d  } | d | d	 f | k r| d	 7} qqWWd  QXd
 } d | } d | } | | k r|| n | } d | | | } | d k  rŠd } n  d t |  d GHi  } t } t d  i } x_ | D]W } | j	   j
 d  } | d | d	 f | k r.t | | d | d	 f <qŢt } PqŢWWd  QX| rMd GHn d GHd  S(   Ns   ../keys.jsons   graph_auto.csvi   t   PoloChaus   ../solution.csvt   rt   ,i    i   i(   g      y@g      <@g        s   Points for Part 1.1b:   s   
s	   graph.csvs   Grade for 1.1c:   0s   Grade for 1.1c:   2(   t   scriptt   loadKeyst   tweepyt   OAuthHandlert   set_access_tokent   APIt   GatherAllEdgest   writeToFilet   opent   stript   splitt   Truet   strt   False(   t   KEY_FILEt   OUTPUT_FILE_GRAPHt   NO_OF_NEIGHBOURSt	   ROOT_USERt   api_keyt
   api_secrett   tokent   token_secrett   autht   apit   edgest   solution_dictt   solutiont   linet   countt
   autogradedt	   thresholdt   rest	   scoreEacht   sub_valt   repeatt   flagt   stu_file(    (    s   hw1_q1_grader.pyt   main	   sR    "

	t   __main__(   t   csvt   jsont   timeR   R   R(   t   __name__(    (    (    s   hw1_q1_grader.pyt   <module>   s   	6