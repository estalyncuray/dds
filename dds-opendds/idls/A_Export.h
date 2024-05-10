
// -*- C++ -*-
// Definition for Win32 Export directives.
// This file is generated automatically by generate_export_file.pl A
// ------------------------------
#ifndef A_EXPORT_H
#define A_EXPORT_H

#include "ace/config-all.h"

#if defined (ACE_AS_STATIC_LIBS) && !defined (A_HAS_DLL)
#  define A_HAS_DLL 0
#endif /* ACE_AS_STATIC_LIBS && A_HAS_DLL */

#if !defined (A_HAS_DLL)
#  define A_HAS_DLL 1
#endif /* ! A_HAS_DLL */

#if defined (A_HAS_DLL) && (A_HAS_DLL == 1)
#  if defined (A_BUILD_DLL)
#    define A_Export ACE_Proper_Export_Flag
#    define A_SINGLETON_DECLARATION(T) ACE_EXPORT_SINGLETON_DECLARATION (T)
#    define A_SINGLETON_DECLARE(SINGLETON_TYPE, CLASS, LOCK) ACE_EXPORT_SINGLETON_DECLARE(SINGLETON_TYPE, CLASS, LOCK)
#  else /* A_BUILD_DLL */
#    define A_Export ACE_Proper_Import_Flag
#    define A_SINGLETON_DECLARATION(T) ACE_IMPORT_SINGLETON_DECLARATION (T)
#    define A_SINGLETON_DECLARE(SINGLETON_TYPE, CLASS, LOCK) ACE_IMPORT_SINGLETON_DECLARE(SINGLETON_TYPE, CLASS, LOCK)
#  endif /* A_BUILD_DLL */
#else /* A_HAS_DLL == 1 */
#  define A_Export
#  define A_SINGLETON_DECLARATION(T)
#  define A_SINGLETON_DECLARE(SINGLETON_TYPE, CLASS, LOCK)
#endif /* A_HAS_DLL == 1 */

// Set A_NTRACE = 0 to turn on library specific tracing even if
// tracing is turned off for ACE.
#if !defined (A_NTRACE)
#  if (ACE_NTRACE == 1)
#    define A_NTRACE 1
#  else /* (ACE_NTRACE == 1) */
#    define A_NTRACE 0
#  endif /* (ACE_NTRACE == 1) */
#endif /* !A_NTRACE */

#if (A_NTRACE == 1)
#  define A_TRACE(X)
#else /* (A_NTRACE == 1) */
#  if !defined (ACE_HAS_TRACE)
#    define ACE_HAS_TRACE
#  endif /* ACE_HAS_TRACE */
#  define A_TRACE(X) ACE_TRACE_IMPL(X)
#  include "ace/Trace.h"
#endif /* (A_NTRACE == 1) */

#endif /* A_EXPORT_H */

// End of auto generated file.
